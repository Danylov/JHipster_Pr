import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { GatewaySharedLibsModule, GatewaySharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [GatewaySharedLibsModule, GatewaySharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [GatewaySharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewaySharedModule {
  static forRoot() {
    return {
      ngModule: GatewaySharedModule
    };
  }
}
