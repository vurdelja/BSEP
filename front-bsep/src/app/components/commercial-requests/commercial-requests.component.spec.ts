import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommercialRequestsComponent } from './commercial-requests.component';

describe('CommercialRequestsComponent', () => {
  let component: CommercialRequestsComponent;
  let fixture: ComponentFixture<CommercialRequestsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CommercialRequestsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CommercialRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
