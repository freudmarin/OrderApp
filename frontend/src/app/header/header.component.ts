import { Component, OnInit } from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {LocalStorageService} from 'ngx-webstorage';
import {UserPayload} from "../user/user-payload";
import {Role} from "../user/role";
import {UserRetrieved} from "../user/user-retrieved";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  user: UserRetrieved;

  constructor(private authenticationService: AuthService) {
    this.authenticationService.user.subscribe(x => this.user = x);
  }

  get isAdmin() {
    return this.user && this.user.role === Role.Admin;
  }

  get isUser() {
    return this.user != null
  }
  ngOnInit(): void {
    }
  logout() {
    this.authenticationService.logout();
  }
}
