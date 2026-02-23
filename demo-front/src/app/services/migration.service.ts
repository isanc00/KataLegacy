import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MigrationResponse, MigrationRequest } from '../models/models';

@Injectable({
  providedIn: 'root',
})
export class MigrationService {
  private http = inject(HttpClient);

  private apiUrl = 'http://localhost:8080/api/v1/migrations/translate';

  translateCode(request: MigrationRequest): Observable<MigrationResponse> {
    return this.http.post<MigrationResponse>(this.apiUrl, request);
  }
}
