import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {CustomerService} from '../customer.service';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss']
})
export class CustomerComponent implements OnInit {

  id: number;
  onUpdate: boolean;
  customerForm: FormGroup;
  submitted = false;

  constructor(private formBuilder: FormBuilder, private customerService: CustomerService, private router: Router,
              private route: ActivatedRoute) {
    this.customerForm = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      address: ['', Validators.required]
    });
  }

  ngOnInit() {
    this.id = this.route.snapshot.params.id;
    if (this.id > 0) {
      this.onUpdate = true;
      this.customerService.get(this.id).subscribe(data => {
        this.customerForm.patchValue(data);
      }, error => console.log(error));
    }
  }

  onSubmit() {
    this.submitted = true;
    if (this.customerForm.valid) {
      if (!this.id) {
        this.customerService.addCustomer(this.customerForm.value).subscribe(() => {
          console.log('customer added');
          this.router.navigateByUrl('/customers');
        });
      } else {
        this.onUpdate = true;
        this.customerService.update(this.id, this.customerForm.value).subscribe(() => {
          console.log('update usccess');
          this.router.navigateByUrl('/customers');
        });
      }
    }
  }

  get customerFormControl() {
    return this.customerForm.controls;
  }
}
