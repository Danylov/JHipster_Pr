/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GatewayTestModule } from '../../../../test.module';
import { EntityaComponent } from 'app/entities/servicea/entitya/entitya.component';
import { EntityaService } from 'app/entities/servicea/entitya/entitya.service';
import { Entitya } from 'app/shared/model/servicea/entitya.model';

describe('Component Tests', () => {
  describe('Entitya Management Component', () => {
    let comp: EntityaComponent;
    let fixture: ComponentFixture<EntityaComponent>;
    let service: EntityaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [EntityaComponent],
        providers: []
      })
        .overrideTemplate(EntityaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EntityaComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EntityaService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Entitya(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.entityas[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
