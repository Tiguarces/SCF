import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {NavbarComponent} from './components/navbar/navbar.component';
import { FooterComponent } from './components/footer/footer.component';
import { HomeComponent } from './components/home/home.component';
import { AccountComponent } from './components/account/account.component';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http'
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { TokenInterceptor } from './util/token.interceptor';
import { LoggedAccountComponent } from './components/logged-account/logged-account.component';

// Material
import { MatTabsModule } from '@angular/material/tabs'
import { MatDialogModule } from '@angular/material/dialog';
import { ProfileSettingsPopUpComponent } from './components/profile-settings-pop-up/profile-settings-pop-up.component';
import { UpdateProfileImagesPopUpComponent } from './components/update-profile-images-pop-up/update-profile-images-pop-up.component';
import { EditProfileDescriptionPopUpComponent } from './components/edit-profile-description-pop-up/edit-profile-description-pop-up.component';

@NgModule({
    declarations: [
        AppComponent,
        NavbarComponent,
        FooterComponent,
        HomeComponent,
        AccountComponent,
        LoggedAccountComponent,
        ProfileSettingsPopUpComponent,
        UpdateProfileImagesPopUpComponent,
        EditProfileDescriptionPopUpComponent
    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpClientModule,
        ReactiveFormsModule,
        BrowserAnimationsModule,
        FontAwesomeModule,
        NgxWebstorageModule.forRoot(),
        ToastrModule.forRoot({
          positionClass: "toast-bottom-left",
          preventDuplicates: true
        }),

        MatTabsModule,
        MatDialogModule
    ],
    providers: [
      {
        provide: HTTP_INTERCEPTORS,
        useClass: TokenInterceptor,
        multi: true
      }
    ],
    bootstrap: [AppComponent]
})
export class AppModule {}
