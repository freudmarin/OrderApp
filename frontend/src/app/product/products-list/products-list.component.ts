import {Component, OnInit} from '@angular/core';
import {RoleManagementService} from "../../role-management.service";
import {Router} from "@angular/router";
import {Product} from "../product";
import {ProductService} from "../product.service";

@Component({
  selector: 'app-products-list',
  templateUrl: './products-list.component.html',
  styleUrls: ['./products-list.component.scss']
})
export class ProductsListComponent implements OnInit {
  products: Product[] = [];
  public roleManagementService: RoleManagementService;

  constructor(public productService: ProductService, private router: Router, roleManagementService: RoleManagementService) {
    this.roleManagementService = roleManagementService;
  }

  ngOnInit() {
    this.retrieveProducts();
  }

  retrieveProducts() {
    this.productService.getAll()
      .subscribe(
        data => {
          this.products = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });

  }

  refreshList() {
    this.retrieveProducts();
  }

  deleteProduct(id: number) {
    this.productService.delete(id)
      .subscribe(
        data => {
          console.log(data);
          this.refreshList();
        },
        error => console.log(error));
  }

  addProduct() {
    this.router.navigate(['product']);
  }

}
