import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoggedAccountComponent } from './logged-account.component';

describe('LoggedAccountComponent', () => {
  let component: LoggedAccountComponent;
  let fixture: ComponentFixture<LoggedAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoggedAccountComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LoggedAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
