import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecurringTransactionComponent  } from './recurring-transaction';

describe('RecurringTransaction', () => {
  let component: RecurringTransactionComponent;
  let fixture: ComponentFixture<RecurringTransactionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecurringTransactionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecurringTransactionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
