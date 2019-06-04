import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IEntitya } from 'app/shared/model/servicea/entitya.model';

type EntityResponseType = HttpResponse<IEntitya>;
type EntityArrayResponseType = HttpResponse<IEntitya[]>;

@Injectable({ providedIn: 'root' })
export class EntityaService {
  public resourceUrl = SERVER_API_URL + 'services/servicea/api/entityas';

  constructor(protected http: HttpClient) {}

  create(entitya: IEntitya): Observable<EntityResponseType> {
    return this.http.post<IEntitya>(this.resourceUrl, entitya, { observe: 'response' });
  }

  update(entitya: IEntitya): Observable<EntityResponseType> {
    return this.http.put<IEntitya>(this.resourceUrl, entitya, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEntitya>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEntitya[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
