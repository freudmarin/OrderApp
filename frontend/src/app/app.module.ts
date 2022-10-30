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
import { UsersListComponent } from './user/users-list/users-list.component';
import {AuthInterceptor} from "./auth-interceptor";
//import {AuthGuard} from "./auth.guard";
import {Role} from "./user/role";
import {AuthGuard} from "./auth.guard";

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    UserComponent,
    LoginComponent,
    HomeComponent,
    FooterComponent,
    UsersListComponent
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
      {path: 'home', component: HomeComponent},
    ]), HttpClientModule
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
