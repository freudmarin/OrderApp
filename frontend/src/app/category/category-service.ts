import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Category} from "./category";
import {Page} from "../page";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private categoriesUrl= 'http://localhost:8080/api/categories';

  constructor(private httpClient: HttpClient) {
  }

  getAllPaginated(request) :Observable<Page<Category>> {
    const params = request;
    return this.httpClient.get<Page<Category>>(this.categoriesUrl + '/paginated', {params});
  }

  getAll()  {
    return this.httpClient.get<Category[]>(this.categoriesUrl);
  }

  addCategory(category): Observable<Category> {
    return this.httpClient.post<Category>(this.categoriesUrl + '/add', category);
  }

  get(id) {
    return this.httpClient.get<Category>(`${this.categoriesUrl}/${id}`);
  }

  update(id, category: Category) {
    return this.httpClient.put(`${this.categoriesUrl}/${id}`, category);
  }

  delete(id) {
    return this.httpClient.delete(`${this.categoriesUrl}/${id}`);
  }
}
