import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'entitya',
        loadChildren: './servicea/entitya/entitya.module#ServiceaEntityaModule'
      },
      {
        path: 'entityb',
        loadChildren: './serviceb/entityb/entityb.module#ServicebEntitybModule'
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
