import {Component, OnInit} from '@angular/core';
import {UserService} from "../user.service";
import {UserPayload} from "../user-payload";
import {Router} from "@angular/router";
import {Observable} from "rxjs";

@Component({
  selector: 'app-user-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.scss']
})
export class UsersListComponent implements OnInit {
  users: UserPayload[] = [];

  constructor(private userService: UserService, private router: Router) {
  }

  ngOnInit() {
    this.retrieveUsers();
  }

  retrieveUsers() {
    this.userService.getAll()
      .subscribe(
        data => {
          this.users = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });

  }

  refreshList() {
    this.retrieveUsers();
  }

  deleteUser(id: number) {
    this.userService.delete(id)
      .subscribe(
        data => {
          console.log(data);
          this.refreshList();
        },
        error => console.log(error));
  }

  addUser() {
    this.router.navigate(['user']);
  }



}
