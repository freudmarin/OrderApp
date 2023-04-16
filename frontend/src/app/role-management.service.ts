import { Injectable } from '@angular/core';
import {UserRetrieved} from "./user/user-retrieved";
import {AuthService} from "./auth/auth.service";
import {ActivatedRoute} from "@angular/router";
import {Role} from "./user/role";

@Injectable({
  providedIn: 'root'
})
export class RoleManagementService {
  user: UserRetrieved;

  constructor(private authenticationService: AuthService) {
    this.authenticationService.user.subscribe(x => this.user = x)
  }

  ngOnInit(): void {

  }
  get isAdmin() {
    return this.user && this.user.role === Role.Admin;
  }

  get isUser() {
    return this.user != null
  }
  logout() {
    this.authenticationService.logout();
  }
}
