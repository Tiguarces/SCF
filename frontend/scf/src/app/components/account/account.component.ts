import { HttpErrorResponse } from '@angular/common/http';
import { Component, ElementRef, Injectable, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LoginRequest } from 'src/app/models/LoginRequest';
import { RegisterRequest } from 'src/app/models/RegisterRequest';
import { LoginResponse } from 'src/app/models/response/LoginResponse';
import { UserResponse } from 'src/app/models/response/UserResponse';
import { AuthService } from 'src/app/services/auth.service';
import { NavbarComponent } from '../navbar/navbar.component';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {
  public loginForm: FormGroup;
  public registerForm: FormGroup;

  private loginFormName: string;
  private passwordFormName: string;
  private nicknameFormName: string;
  private emailFormName: string;

  private userRoleId: number;

  constructor(private notifierService: ToastrService, private authService: AuthService, private router: Router) {
    this.emailFormName = "email";
    this.loginFormName = "login";
    this.passwordFormName = "password";
    this.nicknameFormName = "nickname";

    this.loginForm = new FormGroup({
        login: new FormControl('', [Validators.required, Validators.minLength(1)]),
        password: new FormControl('', [Validators.required, Validators.minLength(1)])
    });

    this.registerForm = new FormGroup({
      login: new FormControl('', [Validators.required, Validators.minLength(1)]),
      password: new FormControl('', [Validators.required, Validators.minLength(1)]),
      nickname: new FormControl('', [Validators.required, Validators.minLength(1)]),
      email: new FormControl('', [Validators.required, Validators.minLength(1), Validators.email]),
    });

    this.authService.getUserRoleId().subscribe({
      next:
        (response: UserResponse) => this.userRoleId = response.userId,
      error:
        (error: HttpErrorResponse) => console.log(error)
    });
  }

  ngOnInit(): void {
  }

  public loginUser(): void {
    let loginFormElm = this.loginForm.get(this.loginFormName);
    let passwordFormElm = this.loginForm.get(this.passwordFormName);

    if(loginFormElm?.valid && passwordFormElm?.valid) {
      const request: LoginRequest = {
        username: loginFormElm.value,
        password: passwordFormElm.value
      };

      this.authService.loginUser(request).subscribe({
        next:
          (response: LoginResponse) => {
            this.notifierService.success("Pomyślnie logowanie", "Logowanie")
            this.router
                  .navigateByUrl("/", { skipLocationChange: true})
                  .then(() => {
                    // Refresh Navbar
                    NavbarComponent.isUserLogged = this.authService.isUserLogged();
                  })
        },
        error:
          (error: HttpErrorResponse) => this.notifierService.warning("Sprawdź poprawność danych", "Logowanie")
      });
    } else {
      this.notifierService.warning("Pola nie mogą być puste, sprawdź poprawność danych", "Logowanie");
    }
  }

  public registerUser(): void {
    let loginFormElm = this.registerForm.get(this.loginFormName);
    let passwordFormElm = this.registerForm.get(this.passwordFormName);
    let nicknameFormElm = this.registerForm.get(this.nicknameFormName);
    let emailFormElm = this.registerForm.get(this.emailFormName);

    if(loginFormElm?.valid && passwordFormElm?.valid &&
      nicknameFormElm?.valid && emailFormElm?.valid && this.userRoleId) {

        const request: RegisterRequest = {
          username: loginFormElm.value,
          password: passwordFormElm.value,
          nickname: nicknameFormElm.value,
          email: emailFormElm.value,
          roleId: this.userRoleId
        };

        this.authService.registerUser(request).subscribe(data => {
          if(data.success) {
            this.notifierService.success("Udana rejestracja", "Rejestracja")
            this.notifierService.info("Email aktywacyjny został wysłany, sprawdź pocztę", "Aktywacja konta")

            emailFormElm?.setValue("");
            loginFormElm?.setValue("");
            passwordFormElm?.setValue("");
            nicknameFormElm?.setValue("");
          } else {
            this.notifierService.warning("Nieudana rejestracja, " + data.message, "Rejestracja");
          }
        });
    } else {
      this.notifierService.warning("Pola nie mogą być puste, sprawdź poprawność danych", "Rejestracja");
    }
  }

}
