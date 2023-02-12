import {Component, Input, OnInit, Output} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {Role} from "../user/role";
import {UserRetrieved} from "../user/user-retrieved";
import {BehaviorSubject, Observable} from "rxjs";
import {ActivatedRoute} from "@angular/router";
import {RoleManagementService} from "../role-management.service";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  public  roleManagementService : RoleManagementService;
  constructor(roleManagementService: RoleManagementService, public  authenticationService: AuthService) {
    this.roleManagementService = roleManagementService;
  }

  ngOnInit(): void {

  }

  logout() {
    this.authenticationService.logout();
  }
}
