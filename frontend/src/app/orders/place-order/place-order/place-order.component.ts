import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {OrderRequest, OrdersService} from '../../order/order.service';
import {CustomerService} from '../../../customer/customer.service';
import {Customer} from '../../../customer/customer';
import {ActivatedRoute, Router} from '@angular/router';
import {Product} from '../../../product/product';
import {ProductService} from '../../../product/product.service';
import {MatDialog} from '@angular/material/dialog';
import {DialogComponent} from '../../../utils/dialog.component';

@Component({
  selector: 'app-place-order',
  templateUrl: './place-order.component.html',
  styleUrls: ['./place-order.component.scss']
})

export class PlaceOrderComponent implements OnInit {

  placeOrderForm: FormGroup;
  customers: Customer[];
  products: Product[];
  @Output() placeOrderEvent = new EventEmitter<OrderRequest>();

  constructor(private formBuilder: FormBuilder, private ordersService: OrdersService,
              private customerService: CustomerService, private productService: ProductService,
              private router: Router, private route: ActivatedRoute, private dialog: MatDialog) {
    this.placeOrderForm = this.formBuilder.group({
      customerId: ['', Validators.required],
      items: this.formBuilder.array([this.createItemFormGroup()]),
    });
  }

  ngOnInit(): void {
    this.customerService.getAll().subscribe(data => {
      this.customers = data;
    });

    this.productService.getAll().subscribe(data => {
      this.products = data;
    });
  }

  createItemFormGroup(): FormGroup {
    return this.formBuilder.group({
      productCode: ['', Validators.required],
      quantity: ['', [Validators.required, Validators.min(1)]],
    });
  }

  get items(): FormArray {
    return this.placeOrderForm.get('items') as FormArray;
  }

  addItem(): void {
    this.items.push(this.createItemFormGroup());
  }

  removeItem(index: number): void {
    this.items.removeAt(index);
  }

  onSubmit(): void {
    if (this.placeOrderForm.valid) {
      const orderReq: OrderRequest = this.placeOrderForm.value;
      if (this.products.some(product => product.unitInStock <= 0)) {
        this.dialog.open(DialogComponent, {
          data: {title: 'Order not processed', message: 'There is not enough stock left'},
          panelClass: 'custom-dialog'
        });
      } else {
        console.log(orderReq);
        this.ordersService.placeOrder(orderReq).subscribe(() => {
          this.placeOrderForm.reset();
          this.items.clear();
          this.items.push(this.createItemFormGroup());
          this.router.navigateByUrl('/orders');
        }, onerror => {
          this.dialog.open(DialogComponent, {
            data: {title: 'Order not processed', message: 'There is not enough stock left'},
            panelClass: 'custom-dialog'
          });
        });
      }
    }
  }
}
