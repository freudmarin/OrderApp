import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from "../user.service";
import {UserPayload} from "../user-payload";
import {CustomValidationService} from "../custom-validation.service";

@Component({
  selector: 'app-register',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  id: number;
  onUpdate: boolean;
  userForm: FormGroup;
  userPayload: UserPayload;
  roles: String[];
  submitted = false;

  constructor(private formBuilder: FormBuilder, private userService: UserService, private router: Router,
              private route: ActivatedRoute, private customValidator: CustomValidationService) {

    this.userForm = this.formBuilder.group({
      username: ['', Validators.required],
      fullName: ['', Validators.required],
      password: ['', Validators.required],
      role: ['', Validators.required],
      jobTitle: ['', Validators.required],
      confirmPassword: ['', Validators.required],
    }, {
      validator: this.customValidator.MatchPassword('password', 'confirmPassword'),
    });

    // @ts-ignore
  }

  ngOnInit() {
    this.id = this.route.snapshot.params.id;
    if (this.id > 0) {
      this.onUpdate = true;
      this.userService.get(this.id).subscribe(data => {
        this.userForm.patchValue(data);
      }, error => console.log(error));
    }
    this.userService.getRoles().subscribe(data => {
      this.roles = data;
    });
  }

  onSubmit() {
    this.submitted = true;
    if (!this.id) {
      this.userService.addUser(this.userForm.value).subscribe(data => {
        console.log('user added');
        this.router.navigateByUrl('/users');
      });
    } else {
      this.onUpdate = true;
      this.userService.update(this.id, this.userForm.value).subscribe(data => {
        console.log('update usccess');
        this.router.navigateByUrl('/users');
      });
    }
  }

  get userFormControl() {
    return this.userForm.controls;
  }
}
