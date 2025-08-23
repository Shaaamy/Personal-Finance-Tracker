import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecurringTransaction } from './recurring-transaction';

describe('RecurringTransaction', () => {
  let component: RecurringTransaction;
  let fixture: ComponentFixture<RecurringTransaction>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecurringTransaction]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecurringTransaction);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
