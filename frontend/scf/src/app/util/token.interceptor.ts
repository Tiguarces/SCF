import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { AuthService } from '../services/auth.service';
import { catchError, Observable, of } from 'rxjs';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

  private allowedRoutes: string[];

  constructor(private authService: AuthService, private router: Router, private notifier: ToastrService) {
    this.allowedRoutes = [
      "/topic/category/all",
      "/answer/all/last",
      "/topic/all/last",
      "/auth/token/isExpired"
    ];
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    for(let i = 0; i < this.allowedRoutes.length; i++) {
      if(this.checkURL(this.allowedRoutes[i], request)) {
        return next.handle(request);
      }
    }

    if(this.authService.isUserLogged()) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.authService.getAccessToken()}`
        }
      })
    } return next.handle(request).pipe(
      catchError((error, caught) => {
        if(error instanceof HttpErrorResponse && error.status === 401) {
          if(this.authService.isUserLogged()) {
            this.refreshToken();
          }
        } throw error;
      })
    );
  }

  private refreshToken(): void {
    this.authService.refreshToken().subscribe({
      error:
        (error: HttpErrorResponse) => {
          this.notifier.info("Twoja sesja wygasła, zaloguj się ponownie", "Sesja")
          this.router.navigateByUrl("/account");
          this.authService.logoutUser().subscribe();
        }
    });
  }

  private checkURL(desiredUrl: string, request: HttpRequest<any>): boolean {
    return request.url.indexOf(desiredUrl) !== -1;
  }
}
