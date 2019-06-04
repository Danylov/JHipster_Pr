import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEntitya } from 'app/shared/model/servicea/entitya.model';
import { EntityaService } from './entitya.service';

@Component({
  selector: 'jhi-entitya-delete-dialog',
  templateUrl: './entitya-delete-dialog.component.html'
})
export class EntityaDeleteDialogComponent {
  entitya: IEntitya;

  constructor(protected entityaService: EntityaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.entityaService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'entityaListModification',
        content: 'Deleted an entitya'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-entitya-delete-popup',
  template: ''
})
export class EntityaDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ entitya }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(EntityaDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.entitya = entitya;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/entitya', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/entitya', { outlets: { popup: null } }]);
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
