/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { EntitybDetailComponent } from 'app/entities/serviceb/entityb/entityb-detail.component';
import { Entityb } from 'app/shared/model/serviceb/entityb.model';

describe('Component Tests', () => {
  describe('Entityb Management Detail Component', () => {
    let comp: EntitybDetailComponent;
    let fixture: ComponentFixture<EntitybDetailComponent>;
    const route = ({ data: of({ entityb: new Entityb(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [EntitybDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(EntitybDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EntitybDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.entityb).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
