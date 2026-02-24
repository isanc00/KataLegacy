import { Injectable } from '@angular/core';


@Injectable({ providedIn: 'root' })
export class RequestContextService {

  generateTransactionId(): string {
    return `TRX-${crypto.randomUUID()}`;
  }
}
