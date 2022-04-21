import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { map, Observable, of, tap } from 'rxjs';
import { LoginRequest } from '../models/LoginRequest';
import { RegisterRequest } from '../models/RegisterRequest';
import { UniversalResponse } from '../models/response/UniversalResponse';
import { LoginResponse } from '../models/response/LoginResponse';
import { RefreshTokenResponse } from '../models/response/RefreshTokenResponse'
import { UserResponse } from '../models/response/UserResponse';
import jwtDecode from 'jwt-decode';
import { JwtToken } from '../models/JwtToken';
import { NavbarComponent } from '../components/navbar/navbar.component';
import { ExtendedAppUserResponse } from '../models/response/ExtendedAppUserResponse';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private decodedToken: JwtToken;
  private refreshTokenField: string;

  private readonly api: string;
  private readonly accessTokenLSWord: string;

  constructor(private http: HttpClient, private localStorage: LocalStorageService) {
    this.api = "http://localhost:8080";
    this.accessTokenLSWord = "accessToken";
  }

  public registerUser(request: RegisterRequest): Observable<UniversalResponse> {
    return this.http.post<UniversalResponse>(`${this.api}/auth/register`, request);
  }

  public getUserRoleId(): Observable<UserResponse> {
    return this.http.get<UserResponse>(`${this.api}/roles/get/user/id`);
  }

  public loginUser(request: LoginRequest): Observable<LoginResponse>{
    return this.http.post<LoginResponse>(`${this.api}/auth/login`, request).pipe(
      map(response => {
        this.localStorage.store(this.accessTokenLSWord, response.accessToken);
        this.refreshTokenField = response.refreshToken;
        return response;
      })
    )
  }

  private decodeToken(): void {
    this.decodedToken = jwtDecode(this.getAccessToken());
  }

  public logoutUser(): Observable<UniversalResponse> {
    return this.http.get<UniversalResponse>(`${this.api}/auth/logout`).pipe(
      map(response => {
        if(response.success) {
          this.localStorage.clear(this.accessTokenLSWord);
          this.refreshTokenField = "";

          NavbarComponent.isUserLogged = this.isUserLogged();
        } return response;
      })
    );
  }

  public refreshToken(): Observable<RefreshTokenResponse> {
    const refreshTokenRequest = {
      refreshToken: this.getRefreshToken()
    }

    return this.http.post<RefreshTokenResponse>(`${this.api}/auth/refresh`, refreshTokenRequest).pipe(
      map(response => {
        this.localStorage.store(this.accessTokenLSWord, response.accessToken);
        this.refreshTokenField = response.refreshToken;
        return response;
      })
    );
  }

  public getLoggedUser(): Observable<ExtendedAppUserResponse> {
    this.decodeToken();
    return this.http.get<ExtendedAppUserResponse>(`${this.api}/auth/user/username/${this.decodedToken.sub}`);
  }

  public tokenIsExpired(): Observable<boolean> {
    return this.http.get<boolean>(`${this.api}/auth/token/isExpired/${this.getAccessToken()}`);
  }

  ///////////////////////////////////////////

  public isUserLogged(): boolean {
    return this.getAccessToken() !== null;
  }

  public getAccessToken(): string {
    return this.localStorage.retrieve("accessToken");
  }

  public getRefreshToken(): string {
    return this.refreshTokenField;
  }

}
