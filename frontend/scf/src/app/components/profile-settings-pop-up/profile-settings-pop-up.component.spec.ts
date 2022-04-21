import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileSettingsPopUpComponent } from './profile-settings-pop-up.component';

describe('ProfileSettingsPopUpComponent', () => {
  let component: ProfileSettingsPopUpComponent;
  let fixture: ComponentFixture<ProfileSettingsPopUpComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ProfileSettingsPopUpComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ProfileSettingsPopUpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
