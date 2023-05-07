import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {OrderResponse, OrdersService} from './order.service';
import {RoleManagementService} from '../../role-management.service';
import {Router} from '@angular/router';
import {tap} from 'rxjs/operators';
import {MatPaginator} from '@angular/material/paginator';
import {OrderDataSource} from './order-data-source';

@Component({
  selector: 'app-orders',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
})
export class OrderComponent implements OnInit, AfterViewInit {
  orders: OrderResponse[] = [];
  public roleManagementService: RoleManagementService;
  orderDatasource: OrderDataSource;
  public displayedColumns: string[];
  constructor(public ordersService: OrdersService, roleManagementService: RoleManagementService,
              private router: Router) {
    this.roleManagementService = roleManagementService;
  }

  pageSize = 5;
  currentPage = 0;

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  ngOnInit() {
    this.displayedColumns = this.roleManagementService.isAdmin
      ? ['orderId', 'customerName', 'items']
      : ['orderId', 'customerName', 'items'];
    this.orderDatasource = new OrderDataSource(this.ordersService, this.roleManagementService);
    this.orderDatasource.loadOrders(this.currentPage, this.pageSize);
  }
  refreshList() {
    this.orderDatasource.loadOrders(this.currentPage, this.pageSize);
  }

  ngAfterViewInit() {
    this.orderDatasource.counter$
      .pipe(
        tap((count) => {
          this.paginator.length = count;
        })
      )
      .subscribe();

    this.paginator.page
      .pipe(
        tap(() => this.loadOrders())
      )
      .subscribe();
  }

  loadOrders() {
    this.orderDatasource.loadOrders(this.paginator.pageIndex, this.paginator.pageSize);
  }
/*  deleteProduct(id: number) {
    this.ordersService.delete(id)
      .subscribe(
        data => {
          console.log(data);
          this.refreshList();
        },
        error => console.log(error));
  }*/

  displayPlaceOrder() {
    this.router.navigate(['place-order']);
  }
}
