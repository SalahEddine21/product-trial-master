import { Injectable, inject, signal } from "@angular/core";
import { Product } from "./product.model";
import { HttpClient } from "@angular/common/http";
import { catchError, Observable, of, tap, map } from "rxjs";
import { PaginatedData } from "app/models/paginated-data";

@Injectable({
    providedIn: "root"
}) export class ProductsService {

    private readonly http = inject(HttpClient);
    private readonly path = "/api/products";
    
    private readonly _products = signal<Product[]>([]);

    public readonly products = this._products.asReadonly();

    public get(page: number, size: number): Observable<PaginatedData<Product>> {
        return this.http.get<PaginatedData<Product>>(`${this.path}?page=${page}&size=${size}`).pipe(
            catchError((error): Observable<PaginatedData<Product>> => {
              console.info("Cannot get products from api, calling them from static file", error);
              return this.http.get<Product[]>("assets/products.json").pipe(
                map((products: Product[]): PaginatedData<Product> => {
                  return {
                    content: products,
                    totalElements: products.length,
                    size: products.length,
                    number: 0,
                  };
                })
              );
            }),
            tap((response) => this._products.set(response.content))
          );                 
    }

    public create(product: Product): Observable<boolean> {
        return this.http.post<boolean>(this.path, product).pipe(
            catchError(() => {
                return of(true);
            }),
            tap(() => this._products.update(products => [product, ...products])),
        );
    }

    public update(product: Product): Observable<boolean> {
        return this.http.patch<boolean>(`${this.path}/${product.id}`, product).pipe(
            catchError(() => {
                return of(true);
            }),
            tap(() => this._products.update(products => {
                return products.map(p => p.id === product.id ? product : p)
            })),
        );
    }

    public delete(productId: number): Observable<boolean> {
        return this.http.delete<boolean>(`${this.path}/${productId}`).pipe(
            catchError(() => {
                return of(true);
            }),
            tap(() => this._products.update(products => products.filter(product => product.id !== productId))),
        );
    }
}