/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { EntityaUpdateComponent } from 'app/entities/servicea/entitya/entitya-update.component';
import { EntityaService } from 'app/entities/servicea/entitya/entitya.service';
import { Entitya } from 'app/shared/model/servicea/entitya.model';

describe('Component Tests', () => {
  describe('Entitya Management Update Component', () => {
    let comp: EntityaUpdateComponent;
    let fixture: ComponentFixture<EntityaUpdateComponent>;
    let service: EntityaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [EntityaUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(EntityaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EntityaUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EntityaService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Entitya(123);
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
        const entity = new Entitya();
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
