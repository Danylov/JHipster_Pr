import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Entitya } from 'app/shared/model/servicea/entitya.model';
import { EntityaService } from './entitya.service';
import { EntityaComponent } from './entitya.component';
import { EntityaDetailComponent } from './entitya-detail.component';
import { EntityaUpdateComponent } from './entitya-update.component';
import { EntityaDeletePopupComponent } from './entitya-delete-dialog.component';
import { IEntitya } from 'app/shared/model/servicea/entitya.model';

@Injectable({ providedIn: 'root' })
export class EntityaResolve implements Resolve<IEntitya> {
  constructor(private service: EntityaService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IEntitya> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Entitya>) => response.ok),
        map((entitya: HttpResponse<Entitya>) => entitya.body)
      );
    }
    return of(new Entitya());
  }
}

export const entityaRoute: Routes = [
  {
    path: '',
    component: EntityaComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Entityas'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: EntityaDetailComponent,
    resolve: {
      entitya: EntityaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Entityas'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: EntityaUpdateComponent,
    resolve: {
      entitya: EntityaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Entityas'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: EntityaUpdateComponent,
    resolve: {
      entitya: EntityaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Entityas'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const entityaPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: EntityaDeletePopupComponent,
    resolve: {
      entitya: EntityaResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Entityas'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
