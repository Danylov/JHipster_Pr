/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { GatewayTestModule } from '../../../../test.module';
import { EntitybDeleteDialogComponent } from 'app/entities/serviceb/entityb/entityb-delete-dialog.component';
import { EntitybService } from 'app/entities/serviceb/entityb/entityb.service';

describe('Component Tests', () => {
  describe('Entityb Management Delete Component', () => {
    let comp: EntitybDeleteDialogComponent;
    let fixture: ComponentFixture<EntitybDeleteDialogComponent>;
    let service: EntitybService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [EntitybDeleteDialogComponent]
      })
        .overrideTemplate(EntitybDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EntitybDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EntitybService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
