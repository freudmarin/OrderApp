import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Category} from "./category";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private usersUrl = 'http://localhost:8080/api/categories';

  constructor(private httpClient: HttpClient) {
  }

  getAll() {
    return this.httpClient.get<Category[]>(this.usersUrl + '/');
  }
  addCategory(category): Observable<Category> {
    return this.httpClient.post<Category>(this.usersUrl + '/add', category);
  }

  get(id) {
    return this.httpClient.get<Category>(`${this.usersUrl}/${id}`);
  }

  update(id, category: Category) {
    return this.httpClient.put(`${this.usersUrl}/${id}`, category);
  }

  delete(id) {
    return this.httpClient.delete(`${this.usersUrl}/${id}`);
  }
}
