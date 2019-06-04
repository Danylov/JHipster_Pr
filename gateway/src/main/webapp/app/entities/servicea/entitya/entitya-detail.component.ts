import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEntitya } from 'app/shared/model/servicea/entitya.model';

@Component({
  selector: 'jhi-entitya-detail',
  templateUrl: './entitya-detail.component.html'
})
export class EntityaDetailComponent implements OnInit {
  entitya: IEntitya;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ entitya }) => {
      this.entitya = entitya;
    });
  }

  previousState() {
    window.history.back();
  }
}
