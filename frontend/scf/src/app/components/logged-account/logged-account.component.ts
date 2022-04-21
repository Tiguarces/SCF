import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ExtendedAppUser } from 'src/app/models/ExtendedAppUser';
import { ExtendedAppUserResponse } from 'src/app/models/response/ExtendedAppUserResponse';
import { UniversalResponse } from 'src/app/models/response/UniversalResponse';
import { AuthService } from 'src/app/services/auth.service';
import { EditProfileDescriptionPopUpComponent } from '../edit-profile-description-pop-up/edit-profile-description-pop-up.component';

import { ProfileSettingsPopUpComponent } from '../profile-settings-pop-up/profile-settings-pop-up.component'
import { UpdateProfileImagesPopUpComponent} from '../update-profile-images-pop-up/update-profile-images-pop-up.component'

@Component({
  selector: 'app-logged-account',
  templateUrl: './logged-account.component.html',
  styleUrls: ['./logged-account.component.css']
})
export class LoggedAccountComponent implements OnInit {
  public loggedUser: ExtendedAppUser;

  constructor(private authService: AuthService, private notifier: ToastrService,
              private router: Router, private dialog: MatDialog) {
    this.prepareEmptyObjects();

    this.authService.getLoggedUser().subscribe({
      next:
        (response: ExtendedAppUserResponse) => this.prepareResponse(response),
      error:
        (error: HttpErrorResponse) => console.log(error)
    });
  }

  private prepareResponse(response: ExtendedAppUserResponse): void {
    this.loggedUser = response.appUser;
    this.loggedUser.topics.forEach(topic => {
      if(topic.description.length >= 100)
        topic.description = topic.description.substring(0, 100);
    });
  }

  private prepareEmptyObjects(): void {
    this.loggedUser = {} as ExtendedAppUser;
    this.loggedUser.topics = [];
    this.loggedUser.answers = [];
  }

  ngOnInit(): void {

  }

  public logoutUser(): void {
    const logoutTitle: string = "Wylogowywanie";

    this.authService.logoutUser().subscribe({
      next:
        (response: UniversalResponse) => {
          if(response.success)
            this.notifier.success("Pomyślnie wylogowano", logoutTitle);
          else
            this.notifier.warning("Wystąpił błąd", logoutTitle);
        },
      error:
        (error: HttpErrorResponse) => this.notifier.error(`Błąd serwera, ${error}`, logoutTitle),
      complete:
        () => this.router.navigateByUrl("/account")
    });
  }

  public showProfileSettings(): void {
    this.dialog.open(ProfileSettingsPopUpComponent);
  }

  public showUpdateProfileImages(): void {
    this.dialog.open(UpdateProfileImagesPopUpComponent);
  }

  public showEditProfileDescription(): void {
    this.dialog.open(EditProfileDescriptionPopUpComponent);
  }
}
