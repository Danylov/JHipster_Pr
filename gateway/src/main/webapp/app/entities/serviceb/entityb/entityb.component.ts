import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IEntityb } from 'app/shared/model/serviceb/entityb.model';
import { AccountService } from 'app/core';
import { EntitybService } from './entityb.service';

@Component({
  selector: 'jhi-entityb',
  templateUrl: './entityb.component.html'
})
export class EntitybComponent implements OnInit, OnDestroy {
  entitybs: IEntityb[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected entitybService: EntitybService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.entitybService
      .query()
      .pipe(
        filter((res: HttpResponse<IEntityb[]>) => res.ok),
        map((res: HttpResponse<IEntityb[]>) => res.body)
      )
      .subscribe(
        (res: IEntityb[]) => {
          this.entitybs = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInEntitybs();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IEntityb) {
    return item.id;
  }

  registerChangeInEntitybs() {
    this.eventSubscriber = this.eventManager.subscribe('entitybListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
