import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { ExtendedAppUser } from 'src/app/models/ExtendedAppUser';
import { ExtendedAppUserResponse } from 'src/app/models/response/ExtendedAppUserResponse';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-profile-settings-pop-up',
  templateUrl: './profile-settings-pop-up.component.html',
  styleUrls: ['./profile-settings-pop-up.component.css']
})
export class ProfileSettingsPopUpComponent implements OnInit {
  public loggedUser: ExtendedAppUser;

  constructor(private authService: AuthService, private notifier: ToastrService) {
    this.prepareEmptyObjects();

    authService.getLoggedUser().subscribe({
      next:
        (response: ExtendedAppUserResponse) => this.loggedUser = response.appUser,
      error:
        (error: HttpErrorResponse) => console.log(error)
    });
  }

  private prepareEmptyObjects(): void{
    this.loggedUser = {} as ExtendedAppUser;
    this.loggedUser.answers = [];
    this.loggedUser.topics = [];
  }

  ngOnInit(): void {
  }

}
