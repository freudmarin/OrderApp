import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Product} from './product';
import {Page} from '../page';
import {map, switchMap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private productsUrl = 'http://localhost:8080/api/products';

  constructor(private httpClient: HttpClient) {
  }

  getAllPaginated(request): Observable<Page<Product>> {
    const params = request;
    return this.httpClient.get<Page<Product>>(this.productsUrl + '/paginated', {params});
  }

  getAll() {
    return this.httpClient.get<Product[]>(this.productsUrl);
  }

  addProduct(product): Observable<Product> {
    return this.httpClient.post<Product>(this.productsUrl + '/add', product);
  }

  get(id) {
    return this.httpClient.get<Product>(`${this.productsUrl}/${id}`);
  }

  update(id, product: Product) {
    return this.httpClient.put(`${this.productsUrl}/${id}`, product);
  }

  delete(id) {
    return this.httpClient.delete(`${this.productsUrl}/${id}`);
  }

  isProductCodeNotUnique(productCode: string, id: number): Observable<boolean> {
    if (id > 0) {
      return of(false);
    }
    return this.httpClient.get<boolean>(`${this.productsUrl}/validateCode/${productCode}`);
  }
}
