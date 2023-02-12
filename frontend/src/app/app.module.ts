import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './header/header.component';
import {UserComponent} from './user/user-details/user.component';
import {LoginComponent} from './auth/login/login.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {Ng2Webstorage} from 'ngx-webstorage';
import {HomeComponent} from './home/home.component';
import {FooterComponent} from "./footer/footer.component";
import {UsersListComponent} from './user/users-list/users-list.component';
import {AuthInterceptor} from "./auth-interceptor";
//import {AuthGuard} from "./auth.guard";
import {Role} from "./user/role";
import {AuthGuard} from "./auth.guard";
import {CategoriesListComponent} from './category/categories-list/categories-list.component';
import { CategoryComponent } from './category/category/category.component';
import { CustomerComponent } from './customer/customer/customer.component';
import {CustomersListComponent} from "./customer/customers-list/customers-list.component";
import { ProductsListComponent } from './product/products-list/products-list.component';
import { ProductComponent } from './product/product/product.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    UserComponent,
    LoginComponent,
    HomeComponent,
    FooterComponent,
    UsersListComponent,
    CategoriesListComponent,
    CategoryComponent,
    CustomersListComponent,
    CustomerComponent,
    ProductsListComponent,
    ProductComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    Ng2Webstorage.forRoot(),
    RouterModule.forRoot([
      {path: '', component: HomeComponent},
      {path: 'users', component: UsersListComponent, canActivate: [AuthGuard],  data: { roles: [Role.Admin] }},
      {path: 'user', component: UserComponent, canActivate: [AuthGuard],  data: { roles: [Role.Admin] }},
      {path: 'user/:id', component: UserComponent, canActivate: [AuthGuard],  data: { roles: [Role.Admin] }},
      {path: 'login', component: LoginComponent},
      {path: 'categories', component: CategoriesListComponent, canActivate: [AuthGuard],  data: { roles: [Role.Admin, Role.User] }},
      {path: 'category', component: CategoryComponent, canActivate: [AuthGuard],  data: { roles: [Role.Admin] }},
      {path: 'category/:id', component: CategoryComponent, canActivate: [AuthGuard],  data: { roles: [Role.Admin] }},
      {path: 'customers', component: CustomersListComponent, canActivate: [AuthGuard],  data: { roles: [Role.Admin, Role.User] }},
      {path: 'customer', component: CustomerComponent, canActivate: [AuthGuard],  data: { roles: [Role.User] }},
      {path: 'customer/:id', component: CustomerComponent, canActivate: [AuthGuard],  data: { roles: [Role.User] }},
      {path: 'products', component:ProductsListComponent, canActivate: [AuthGuard],  data: { roles: [Role.Admin, Role.User] }},
      {path: 'product', component: ProductComponent, canActivate: [AuthGuard],  data: { roles: [Role.User] }},
      {path: 'product/:id', component: ProductComponent, canActivate: [AuthGuard],  data: { roles: [Role.User] }},
      {path: 'login', component: LoginComponent},
      {path: 'home', component: HomeComponent},
    ]), HttpClientModule
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
