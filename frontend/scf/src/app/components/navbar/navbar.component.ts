import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  public static isUserLogged: boolean;
  public NavbarComponent_field = NavbarComponent;

  constructor(private authService: AuthService, private router: Router) {
    NavbarComponent.isUserLogged = this.authService.isUserLogged();
  }

  ngOnInit(): void {
  }

}
