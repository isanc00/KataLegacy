import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { RequestContextService } from './services/request/request-context.service';

export const transactionInterceptor: HttpInterceptorFn = (req, next) => {
  const requestContextService = inject(RequestContextService);
  const transactionId = requestContextService.generateTransactionId();

  let modifiedReq = req.clone({
    setHeaders: {
      'X-Transaction-Id': transactionId,
    },
  });

  if (req.method !== 'GET' && req.body && typeof req.body === 'object') {
    modifiedReq = modifiedReq.clone({
      body: {
        ...(req.body as any),
        transactionCode: transactionId,
      },
    });
  }

  return next(modifiedReq);
};
