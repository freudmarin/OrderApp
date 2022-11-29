import {Component, Input, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {Category} from "../category";
import {CategoryService} from "../category-service";
import {RoleManagementService} from "../../role-management.service";

@Component({
  selector: 'app-categories-list',
  templateUrl: './categories-list.component.html',
  styleUrls: ['./categories-list.component.scss']
})
export class CategoriesListComponent implements OnInit {
  categories: Category[] = [];
  public  roleManagementService : RoleManagementService;
  constructor(public categoryService: CategoryService, private router: Router, roleManagementService: RoleManagementService) {
  this.roleManagementService = roleManagementService;
  }

  ngOnInit() {
    this.retrieveCategories();
  }

  retrieveCategories() {
    this.categoryService.getAll()
      .subscribe(
        data => {
          this.categories = data;
          console.log(data);
        },
        error => {
          console.log(error);
        });

  }

  refreshList() {
    this.retrieveCategories();
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

}
