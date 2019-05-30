import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'entity-a',
        loadChildren: './servicea/entity-a/entity-a.module#ServiceaEntityAModule'
      },
      {
        path: 'entity-b',
        loadChildren: './serviceb/entity-b/entity-b.module#ServicebEntityBModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewayEntityModule {}
