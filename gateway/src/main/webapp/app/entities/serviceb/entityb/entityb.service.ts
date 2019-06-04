import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEntityb } from 'app/shared/model/serviceb/entityb.model';

type EntityResponseType = HttpResponse<IEntityb>;
type EntityArrayResponseType = HttpResponse<IEntityb[]>;

@Injectable({ providedIn: 'root' })
export class EntitybService {
  public resourceUrl = SERVER_API_URL + 'services/serviceb/api/entitybs';

  constructor(protected http: HttpClient) {}

  create(entityb: IEntityb): Observable<EntityResponseType> {
    return this.http.post<IEntityb>(this.resourceUrl, entityb, { observe: 'response' });
  }

  update(entityb: IEntityb): Observable<EntityResponseType> {
    return this.http.put<IEntityb>(this.resourceUrl, entityb, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEntityb>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEntityb[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
