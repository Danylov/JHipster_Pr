/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { EntityaDetailComponent } from 'app/entities/servicea/entitya/entitya-detail.component';
import { Entitya } from 'app/shared/model/servicea/entitya.model';

describe('Component Tests', () => {
  describe('Entitya Management Detail Component', () => {
    let comp: EntityaDetailComponent;
    let fixture: ComponentFixture<EntityaDetailComponent>;
    const route = ({ data: of({ entitya: new Entitya(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [EntityaDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(EntityaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EntityaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.entitya).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
