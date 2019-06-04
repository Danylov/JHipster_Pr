import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared';
import {
  EntitybComponent,
  EntitybDetailComponent,
  EntitybUpdateComponent,
  EntitybDeletePopupComponent,
  EntitybDeleteDialogComponent,
  entitybRoute,
  entitybPopupRoute
} from './';

const ENTITY_STATES = [...entitybRoute, ...entitybPopupRoute];

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    EntitybComponent,
    EntitybDetailComponent,
    EntitybUpdateComponent,
    EntitybDeleteDialogComponent,
    EntitybDeletePopupComponent
  ],
  entryComponents: [EntitybComponent, EntitybUpdateComponent, EntitybDeleteDialogComponent, EntitybDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ServicebEntitybModule {}
