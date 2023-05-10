import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {RoleManagementService} from '../../role-management.service';
import {Router} from '@angular/router';
import {ProductService} from '../product.service';
import {MatPaginator} from '@angular/material/paginator';
import {ProductDataSource} from './product-data-source';
import {tap} from 'rxjs/operators';
import {OrdersService} from '../../orders/order/order.service';

@Component({
  selector: 'app-products-list',
  templateUrl: './products-list.component.html',
  styleUrls: ['./products-list.component.scss']
})
export class ProductsListComponent implements OnInit, AfterViewInit {
  public roleManagementService: RoleManagementService;
  productDatasource: ProductDataSource;
  constructor(public productService: ProductService,
              public orderService: OrdersService,
              private router: Router,
              roleManagementService: RoleManagementService) {
    this.roleManagementService = roleManagementService;
  }

  public displayedColumns: string[];

  pageSize = 5;
  currentPage = 0;

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;

  searchValue: string;
  ngOnInit() {
    this.displayedColumns = this.roleManagementService.isAdmin
      ? ['productCode', 'productName', 'description', 'category', 'unitPrice', 'unitInStock']
      : ['productCode', 'productName', 'description', 'category', 'unitPrice', 'unitInStock', 'actions'];
    this.productDatasource = new ProductDataSource(this.productService);
    this.productDatasource.loadProducts();
  }

  applyFilter() {
    this.productDatasource.loadProducts(this.paginator.pageIndex, this.paginator.pageSize, this.searchValue);
  }

  clearSearch() {
    this.searchValue = '';
    this.applyFilter();
  }

  refreshList() {
    this.productDatasource.loadProducts();
  }

  ngAfterViewInit() {
    this.productDatasource.counter$
      .pipe(
        tap((count) => {
          this.paginator.length = count;
        })
      )
      .subscribe();

    this.paginator.page
      .pipe(
        tap(() => this.loadProducts())
      )
      .subscribe();
  }

  loadProducts() {
    this.productDatasource.loadProducts(this.paginator.pageIndex, this.paginator.pageSize);
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
