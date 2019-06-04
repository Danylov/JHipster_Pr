import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IEntityb, Entityb } from 'app/shared/model/serviceb/entityb.model';
import { EntitybService } from './entityb.service';

@Component({
  selector: 'jhi-entityb-update',
  templateUrl: './entityb-update.component.html'
})
export class EntitybUpdateComponent implements OnInit {
  entityb: IEntityb;
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: []
  });

  constructor(protected entitybService: EntitybService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ entityb }) => {
      this.updateForm(entityb);
      this.entityb = entityb;
    });
  }

  updateForm(entityb: IEntityb) {
    this.editForm.patchValue({
      id: entityb.id,
      name: entityb.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const entityb = this.createFromForm();
    if (entityb.id !== undefined) {
      this.subscribeToSaveResponse(this.entitybService.update(entityb));
    } else {
      this.subscribeToSaveResponse(this.entitybService.create(entityb));
    }
  }

  private createFromForm(): IEntityb {
    const entity = {
      ...new Entityb(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEntityb>>) {
    result.subscribe((res: HttpResponse<IEntityb>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
