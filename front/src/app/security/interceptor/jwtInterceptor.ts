import { HttpRequest, HttpHandlerFn, HttpInterceptorFn } from '@angular/common/http';

// Define the JWT interceptor as a function
export const jwtInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn) => {
  if (req.url.includes('auth')) {
    return next(req);
  }

  const token = sessionStorage.getItem('token');
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `${token}`,
      },
    });
  }
  return next(req);
};