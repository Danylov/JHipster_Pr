import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared';
import {
  EntityaComponent,
  EntityaDetailComponent,
  EntityaUpdateComponent,
  EntityaDeletePopupComponent,
  EntityaDeleteDialogComponent,
  entityaRoute,
  entityaPopupRoute
} from './';

const ENTITY_STATES = [...entityaRoute, ...entityaPopupRoute];

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    EntityaComponent,
    EntityaDetailComponent,
    EntityaUpdateComponent,
    EntityaDeleteDialogComponent,
    EntityaDeletePopupComponent
  ],
  entryComponents: [EntityaComponent, EntityaUpdateComponent, EntityaDeleteDialogComponent, EntityaDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ServiceaEntityaModule {}
