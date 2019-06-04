import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IEntitya, Entitya } from 'app/shared/model/servicea/entitya.model';
import { EntityaService } from './entitya.service';

@Component({
  selector: 'jhi-entitya-update',
  templateUrl: './entitya-update.component.html'
})
export class EntityaUpdateComponent implements OnInit {
  entitya: IEntitya;
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: []
  });

  constructor(protected entityaService: EntityaService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ entitya }) => {
      this.updateForm(entitya);
      this.entitya = entitya;
    });
  }

  updateForm(entitya: IEntitya) {
    this.editForm.patchValue({
      id: entitya.id,
      name: entitya.name
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const entitya = this.createFromForm();
    if (entitya.id !== undefined) {
      this.subscribeToSaveResponse(this.entityaService.update(entitya));
    } else {
      this.subscribeToSaveResponse(this.entityaService.create(entitya));
    }
  }

  private createFromForm(): IEntitya {
    const entity = {
      ...new Entitya(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEntitya>>) {
    result.subscribe((res: HttpResponse<IEntitya>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
