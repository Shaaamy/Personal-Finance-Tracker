import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContectUs } from './contect-us';

describe('ContectUs', () => {
  let component: ContectUs;
  let fixture: ComponentFixture<ContectUs>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContectUs]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ContectUs);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
