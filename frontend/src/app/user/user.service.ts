import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {UserPayload} from './user-payload';
import {Page} from '../page';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private usersUrl = 'http://localhost:8080/api/users';

  constructor(private httpClient: HttpClient) {
  }

    getAllPaginated(request): Observable<Page<UserPayload>> {
      const params = request;
      return this.httpClient.get<Page<UserPayload>>(this.usersUrl + '/paginated', {params});
  }

  getAll()  {
    return this.httpClient.get<UserPayload[]>(this.usersUrl);
  }
  addUser(user): Observable<UserPayload> {
    return this.httpClient.post<UserPayload>(this.usersUrl + '/add', user);
  }

  getRoles(): Observable<string[]> {
    return this.httpClient.get<string[]>(this.usersUrl + '/roles');
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

