import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IEntitya } from 'app/shared/model/servicea/entitya.model';
import { AccountService } from 'app/core';
import { EntityaService } from './entitya.service';

@Component({
  selector: 'jhi-entitya',
  templateUrl: './entitya.component.html'
})
export class EntityaComponent implements OnInit, OnDestroy {
  entityas: IEntitya[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected entityaService: EntityaService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.entityaService
      .query()
      .pipe(
        filter((res: HttpResponse<IEntitya[]>) => res.ok),
        map((res: HttpResponse<IEntitya[]>) => res.body)
      )
      .subscribe(
        (res: IEntitya[]) => {
          this.entityas = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInEntityas();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IEntitya) {
    return item.id;
  }

  registerChangeInEntityas() {
    this.eventSubscriber = this.eventManager.subscribe('entityaListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
