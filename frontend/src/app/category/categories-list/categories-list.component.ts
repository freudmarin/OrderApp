import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {Router} from '@angular/router';
import {RoleManagementService} from '../../role-management.service';
import {MatPaginator} from '@angular/material/paginator';
import {tap} from 'rxjs/operators';
import {CategoryService} from '../category-service';
import {CategoryDataSource} from './category-data-source';

@Component({
  selector: 'app-categories-list',
  templateUrl: './categories-list.component.html',
  styleUrls: ['./categories-list.component.scss']
})
export class CategoriesListComponent implements OnInit, AfterViewInit {
  public roleManagementService: RoleManagementService;
  categoryDatasource: CategoryDataSource;

  constructor(public categoryService: CategoryService, private router: Router, roleManagementService: RoleManagementService) {
    this.roleManagementService = roleManagementService;
  }

  public displayedColumns: string[];

  pageSize = 5;
  currentPage = 0;
  searchValue: string;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;

  ngOnInit() {
    this.displayedColumns = this.roleManagementService.isAdmin
      ? ['name', 'actions'] : ['name'];
    this.categoryDatasource = new CategoryDataSource(this.categoryService);
    this.categoryDatasource.loadCategories();
  }

  refreshList() {
    this.categoryDatasource.loadCategories();
  }

  ngAfterViewInit() {
    this.categoryDatasource.counter$
      .pipe(
        tap((count) => {
          this.paginator.length = count;
        })
      )
      .subscribe();

    this.paginator.page
      .pipe(
        tap(() => this.loadCategories())
      )
      .subscribe();
  }

  loadCategories() {
    this.categoryDatasource.loadCategories(this.paginator.pageIndex, this.paginator.pageSize);
  }

  deleteCategory(id: number) {
    this.categoryService.delete(id)
      .subscribe(
        data => {
          console.log(data);
          this.refreshList();
        },
        error => console.log(error));
  }

  addCategory() {
    this.router.navigate(['category']);
  }

  applyFilter() {
    this.categoryDatasource.loadCategories(this.paginator.pageIndex, this.paginator.pageSize, this.searchValue);
  }

  clearSearch() {
    this.searchValue = '';
    this.applyFilter();
  }
}
