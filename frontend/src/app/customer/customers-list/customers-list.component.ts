import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {Customer} from '../customer';
import {RoleManagementService} from '../../role-management.service';
import {CustomerDataSource} from './customer-data-source';
import {CustomerService} from '../customer.service';
import {Router} from '@angular/router';
import {MatPaginator} from '@angular/material/paginator';
import {tap} from 'rxjs/operators';


@Component({
  selector: 'app-customers-list',
  templateUrl: './customers-list.component.html',
  styleUrls: ['./customers-list.component.scss']
})
export class CustomersListComponent implements OnInit, AfterViewInit {
  customers: Customer[] = [];
  public roleManagementService: RoleManagementService;
  customerDatasource: CustomerDataSource;

  constructor(public customerService: CustomerService, private router: Router, roleManagementService: RoleManagementService) {
    this.roleManagementService = roleManagementService;
  }

  public displayedColumns: string[];

  pageSize = 5;
  currentPage = 0;

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  ngOnInit() {
    this.displayedColumns = !this.roleManagementService.isAdmin
      ? ['firstName', 'lastName', 'address', 'actions'] : ['firstName', 'lastName', 'address'];
    this.customerDatasource = new CustomerDataSource(this.customerService);
    this.customerDatasource.loadCustomers();
  }

  refreshList() {
    this.customerDatasource.loadCustomers();
  }

  ngAfterViewInit() {
    this.customerDatasource.counter$
      .pipe(
        tap((count) => {
          this.paginator.length = count;
        })
      )
      .subscribe();

    this.paginator.page
      .pipe(
        tap(() => this.loadCustomers())
      )
      .subscribe();
  }

  loadCustomers() {
    this.customerDatasource.loadCustomers(this.paginator.pageIndex, this.paginator.pageSize);
  }

  deleteCustomer(id: number) {
    this.customerService.delete(id)
      .subscribe(
        data => {
          console.log(data);
          this.refreshList();
        },
        error => console.log(error));
  }

  addCustomer() {
    this.router.navigate(['customer']);
  }
}
