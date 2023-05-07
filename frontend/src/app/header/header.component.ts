import {Component} from '@angular/core';
import {AuthService} from '../auth/auth.service';
import {RoleManagementService} from '../role-management.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent{
  public roleManagementService: RoleManagementService;
  constructor(roleManagementService: RoleManagementService, public  authenticationService: AuthService) {
    this.roleManagementService = roleManagementService;
  }
  logout() {
    this.authenticationService.logout();
  }
}
