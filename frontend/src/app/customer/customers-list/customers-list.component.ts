import {Component, OnInit} from '@angular/core';
import {RoleManagementService} from "../../role-management.service";
import {Router} from "@angular/router";
import {Customer} from "../customer";
import {CustomerService} from "../customer.service";

@Component({
  selector: 'app-customers-list',
  templateUrl: './customers-list.component.html',
  styleUrls: ['./customers-list.component.scss']
})
export class CustomersListComponent implements OnInit {
  customers: Customer[] = [];
  public roleManagementService: RoleManagementService;

  constructor(public customerService: CustomerService, private router: Router, roleManagementService: RoleManagementService) {
    this.roleManagementService = roleManagementService;
  }

  ngOnInit() {
    this.retrieveCustomers();
  }

  retrieveCustomers() {
    this.customerService.getAll()
      .subscribe(
        data => {
          this.customers = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });

  }

  refreshList() {
    this.retrieveCustomers();
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
