import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../auth.service';
import {Router} from '@angular/router';
import {LoginPayload} from '../loginPayload';
import {MatDialog} from '@angular/material/dialog';
import {DialogComponent} from '../../utils/dialog.component';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  loginPayload: LoginPayload;
  isLoggedIn = false;
  submitted: boolean;
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router, public fb: FormBuilder,
              private dialog: MatDialog) {
    this.loginForm = this.fb.group({
      username:  ['', Validators.required],
      password:  ['', Validators.required]
    });
    this.loginPayload = {
      username: '',
      password: ''
    };
  }

  ngOnInit() {
  }

  onSubmit() {
    this.submitted = true;
    this.loginPayload.username = this.loginForm.get('username').value;
    this.loginPayload.password = this.loginForm.get('password').value;
    if (this.loginForm.valid) {
      this.authService.login(this.loginPayload.username, this.loginPayload.password).subscribe((data) => {
        this.isLoggedIn = true;
        console.log('login success');
        this.router.navigateByUrl('/products');
      }, error => {
        this.isLoggedIn = true;
        this.dialog.open(DialogComponent, {
          data: { title: 'Login Failed', message: 'Wrong Credentials'},
          panelClass: 'custom-dialog'
        });
      });
    }
  }

  get loginFormControl() {
    return this.loginForm.controls;
  }
}
