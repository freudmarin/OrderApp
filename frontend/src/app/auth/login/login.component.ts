import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthService} from '../auth.service';
import {Router} from '@angular/router';
import {LoginPayload} from "../loginPayload";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  loginPayload: LoginPayload;
  isLoggedIn = false;
  isLoginFailed = false;
  submitted: boolean;
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router, public fb:FormBuilder) {
    this.loginForm = this.fb.group({
      username:  ['',Validators.required],
      password:  ['',Validators.required]
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
      this.authService.login(this.loginPayload.username, this.loginPayload.password).subscribe(data => {
        this.isLoggedIn = true;
        console.log('login success');
        this.router.navigateByUrl('/home');
      }, error => {
        this.errorMessage = "Login Error, Please enter again your credentials"
        this.isLoginFailed = true;
      });
    }
  }

  get loginFormControl() {
    return this.loginForm.controls;
  }
}
