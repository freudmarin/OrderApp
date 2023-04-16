import {Component, OnInit} from '@angular/core';
import {OrderResponse, OrdersService} from './order.service';
import {RoleManagementService} from "../../role-management.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-orders',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
})
export class OrderComponent implements OnInit {
  orders: OrderResponse[] = [];
  public roleManagementService: RoleManagementService;

  constructor(public ordersService: OrdersService, roleManagementService: RoleManagementService,
              private router: Router) {
    this.roleManagementService = roleManagementService;
  }

  ngOnInit(): void {
    if (this.roleManagementService.isAdmin) {
      this.loadAllOrders();
    } else {
      this.loadOrders();
    }
  }

  loadOrders(): void {
    this.ordersService.getOrdersByUser().subscribe((orders) => {
      this.orders = orders;
    });
  }

  loadAllOrders(): void {
    this.ordersService.getAllOrders().subscribe((orders) => {
      this.orders = orders;
    });
  }

  displayPlaceOrderPage() {
    this.router.navigate(['place-order']);
  }
}
