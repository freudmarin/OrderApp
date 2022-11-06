import {Component, Input, OnInit, Output} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {Role} from "../user/role";
import {UserRetrieved} from "../user/user-retrieved";
import {BehaviorSubject, Observable} from "rxjs";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  user: UserRetrieved;

  constructor(private authenticationService: AuthService,private route: ActivatedRoute) {
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
