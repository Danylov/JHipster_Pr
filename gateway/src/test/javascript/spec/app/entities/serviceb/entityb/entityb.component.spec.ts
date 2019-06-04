/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { GatewayTestModule } from '../../../../test.module';
import { EntitybComponent } from 'app/entities/serviceb/entityb/entityb.component';
import { EntitybService } from 'app/entities/serviceb/entityb/entityb.service';
import { Entityb } from 'app/shared/model/serviceb/entityb.model';

describe('Component Tests', () => {
  describe('Entityb Management Component', () => {
    let comp: EntitybComponent;
    let fixture: ComponentFixture<EntitybComponent>;
    let service: EntitybService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [EntitybComponent],
        providers: []
      })
        .overrideTemplate(EntitybComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EntitybComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EntitybService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Entityb(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.entitybs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
