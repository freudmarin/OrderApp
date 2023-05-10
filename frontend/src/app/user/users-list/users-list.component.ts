import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {UserService} from '../user.service';
import {UserPayload} from '../user-payload';
import {Router} from '@angular/router';
import {RoleManagementService} from '../../role-management.service';
import {MatPaginator} from '@angular/material/paginator';
import {tap} from 'rxjs/operators';
import {UserDataSource} from './user-data-source';

@Component({
  selector: 'app-user-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.scss']
})
export class UsersListComponent implements OnInit, AfterViewInit {
  users: UserPayload[] = [];
  public roleManagementService: RoleManagementService;
  userDatasource: UserDataSource;

  constructor(private userService: UserService, private router: Router, private roleService: RoleManagementService) {
    this.roleManagementService = roleService;
  }

  displayedColumns: string[];
  searchValue: string;
  pageSize = 5;
  currentPage = 0;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  ngOnInit() {
    this.displayedColumns = this.roleManagementService.isAdmin
      ? ['fullName', 'username', 'role', 'jobTitle', 'actions']
      : ['fullName', 'username', 'role', 'jobTitle'];
    this.userDatasource = new UserDataSource(this.userService);
    this.userDatasource.loadUsers();
  }

  applyFilter() {
    this.userDatasource.loadUsers(this.paginator.pageIndex, this.paginator.pageSize, this.searchValue);
  }

  clearSearch() {
    this.searchValue = '';
    this.applyFilter();
  }

  refreshList() {
    this.userDatasource.loadUsers();
  }

  ngAfterViewInit() {
    this.userDatasource.counter$
      .pipe(
        tap((count) => {
          this.paginator.length = count;
        })
      )
      .subscribe();

    this.paginator.page
      .pipe(
        tap(() => this.loadUsers())
      )
      .subscribe();
  }

  loadUsers() {
    this.userDatasource.loadUsers(this.paginator.pageIndex, this.paginator.pageSize);
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
