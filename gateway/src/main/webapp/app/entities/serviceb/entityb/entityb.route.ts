import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Entityb } from 'app/shared/model/serviceb/entityb.model';
import { EntitybService } from './entityb.service';
import { EntitybComponent } from './entityb.component';
import { EntitybDetailComponent } from './entityb-detail.component';
import { EntitybUpdateComponent } from './entityb-update.component';
import { EntitybDeletePopupComponent } from './entityb-delete-dialog.component';
import { IEntityb } from 'app/shared/model/serviceb/entityb.model';

@Injectable({ providedIn: 'root' })
export class EntitybResolve implements Resolve<IEntityb> {
  constructor(private service: EntitybService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IEntityb> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Entityb>) => response.ok),
        map((entityb: HttpResponse<Entityb>) => entityb.body)
      );
    }
    return of(new Entityb());
  }
}

export const entitybRoute: Routes = [
  {
    path: '',
    component: EntitybComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Entitybs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: EntitybDetailComponent,
    resolve: {
      entityb: EntitybResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Entitybs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: EntitybUpdateComponent,
    resolve: {
      entityb: EntitybResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Entitybs'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: EntitybUpdateComponent,
    resolve: {
      entityb: EntitybResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Entitybs'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const entitybPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: EntitybDeletePopupComponent,
    resolve: {
      entityb: EntitybResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Entitybs'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
