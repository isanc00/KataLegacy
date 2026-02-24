import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MigrationResponse, MigrationRequest } from '../models/models';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class MigrationService {

  private http = inject(HttpClient);
  private apiUrl = 'migrations/translate';

  translateCode(request: MigrationRequest): Observable<MigrationResponse> {
    return this.http.post<MigrationResponse>(
      `${environment.apiUrl}/${this.apiUrl}`,
      request,
    );
  }
}
