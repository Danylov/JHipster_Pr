/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { EntitybUpdateComponent } from 'app/entities/serviceb/entityb/entityb-update.component';
import { EntitybService } from 'app/entities/serviceb/entityb/entityb.service';
import { Entityb } from 'app/shared/model/serviceb/entityb.model';

describe('Component Tests', () => {
  describe('Entityb Management Update Component', () => {
    let comp: EntitybUpdateComponent;
    let fixture: ComponentFixture<EntitybUpdateComponent>;
    let service: EntitybService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [EntitybUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(EntitybUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EntitybUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EntitybService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Entityb(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Entityb();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
