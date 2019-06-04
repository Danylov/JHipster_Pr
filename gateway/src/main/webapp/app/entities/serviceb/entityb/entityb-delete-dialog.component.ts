import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEntityb } from 'app/shared/model/serviceb/entityb.model';
import { EntitybService } from './entityb.service';

@Component({
  selector: 'jhi-entityb-delete-dialog',
  templateUrl: './entityb-delete-dialog.component.html'
})
export class EntitybDeleteDialogComponent {
  entityb: IEntityb;

  constructor(protected entitybService: EntitybService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.entitybService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'entitybListModification',
        content: 'Deleted an entityb'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-entityb-delete-popup',
  template: ''
})
export class EntitybDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ entityb }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(EntitybDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.entityb = entityb;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/entityb', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/entityb', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
