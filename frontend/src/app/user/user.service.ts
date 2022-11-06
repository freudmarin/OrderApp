import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpClient} from "@angular/common/http";
import {UserPayload} from "./user-payload";
import {Role} from "./role";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private usersUrl = 'http://localhost:8080/api/users';

  constructor(private httpClient: HttpClient) {
  }

  getAll() {
    return this.httpClient.get<UserPayload[]>(this.usersUrl + '/');
  }
  addUser(user): Observable<UserPayload> {
    return this.httpClient.post<UserPayload>(this.usersUrl + '/add', user);
  }

  getRoles(): Observable<String[]> {
    return this.httpClient.get<String[]>(this.usersUrl + '/roles');
  }

  get(id) {
    return this.httpClient.get<UserPayload>(`${this.usersUrl}/${id}`);
  }

  update(id, userPayload: UserPayload) {
    return this.httpClient.put(`${this.usersUrl}/${id}`, userPayload);
  }

  delete(id) {
    return this.httpClient.delete(`${this.usersUrl}/${id}`);
  }
}

