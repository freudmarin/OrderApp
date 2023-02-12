import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {map} from "rxjs/operators";
import {Router} from "@angular/router";
import {UserRetrieved} from "../user/user-retrieved";


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private url = 'http://localhost:8080/api/auth/';
  private userSubject: BehaviorSubject<UserRetrieved>;
  public user: Observable<UserRetrieved>;
  constructor(private httpClient: HttpClient, private router: Router) {
    this.userSubject = new BehaviorSubject<UserRetrieved>(JSON.parse(localStorage.getItem('user')));
    this.user = this.userSubject.asObservable();
  }

  login(username: String, password: String) {
    return this.httpClient.post<any>(this.url + 'login', {username,password}).pipe(map(user => {
      localStorage.setItem('user', JSON.stringify(user));
      localStorage.setItem('authenticationToken', user.authenticationToken)
      this.userSubject.next(user);
      console.log(user);
      return user;
    }));
  }

  public get userValue(): UserRetrieved {
    return this.userSubject.value;
  }


  logout() {
    localStorage.removeItem('user');
    this.userSubject.next(null);
    this.router.navigate(['/login']);
  }
}
