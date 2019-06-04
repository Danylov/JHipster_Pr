import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEntityb } from 'app/shared/model/serviceb/entityb.model';

@Component({
  selector: 'jhi-entityb-detail',
  templateUrl: './entityb-detail.component.html'
})
export class EntitybDetailComponent implements OnInit {
  entityb: IEntityb;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ entityb }) => {
      this.entityb = entityb;
    });
  }

  previousState() {
    window.history.back();
  }
}
