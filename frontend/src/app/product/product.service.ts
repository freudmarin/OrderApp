import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Product} from "./product";

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private productsUrl = 'http://localhost:8080/api/products';

  constructor(private httpClient: HttpClient) {
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

  update(id, product : Product) {
    return this.httpClient.put(`${this.productsUrl}/${id}`, product);
  }

  delete(id) {
    return this.httpClient.delete(`${this.productsUrl}/${id}`);
  }
}
