import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { LoginRequest } from '../models/LoginRequest';
import { RegisterRequest } from '../models/RegisterRequest';
import { UniversalResponse } from '../models/UniversalResponse';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private api: string;

  constructor(private http: HttpClient) {
    this.api = "http://localhost:8080"
  }

  public registerUser(request: RegisterRequest): Observable<UniversalResponse> {
    return this.http.post<UniversalResponse>(`${this.api}/auth/register`, request);
  }

  public loginUser(request: LoginRequest): Observable<UniversalResponse> {
    let httpParams = new HttpParams()
                                .set("username", request.username)
                                .set("password", request.password);

    return this.http.post<UniversalResponse>(`${this.api}/login`, null, {params: httpParams});
  }
}
