import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ProductService} from '../product.service';
import {CategoryService} from '../../category/category-service';
import {Category} from '../../category/category';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {
  id: number;
  onUpdate: boolean;
  productForm: FormGroup;
  submitted = false;

  categories: Category[];

  constructor(private formBuilder: FormBuilder,
              private categoryService: CategoryService,
              private productService: ProductService, private router: Router,
              private route: ActivatedRoute) {
    this.productForm = this.formBuilder.group({
      productCode: ['', Validators.required],
      productName: ['', Validators.required],
      description: [''],
      category: ['', Validators.required],
      unitPrice: ['', [Validators.required, Validators.min(0)]],
      unitInStock: ['', [Validators.required, Validators.min(1)]],
      discount : ['', [Validators.required, Validators.min(0), Validators.max(100)]]
    });
  }

  ngOnInit() {
    this.id = this.route.snapshot.params.id;
    if (this.id > 0) {
      this.onUpdate = true;
      this.productService.get(this.id).subscribe(data => {
        this.productForm.patchValue(data);
      }, error => console.log(error));
    }
    this.categoryService.getAll().subscribe(data => {
      this.categories = data;
    });
  }

  onSubmit() {
    this.submitted = true;
    if (this.productForm.valid) {
      if (!this.id) {
        this.productService.addProduct(this.productForm.value).subscribe(() => {
          console.log('Product was  added');
          this.router.navigateByUrl('/products');
        });
      } else {
        this.onUpdate = true;
        this.productService.update(this.id, this.productForm.value).subscribe(() => {
          console.log('Update was usccess');
          this.router.navigateByUrl('/products');
        });
      }
    }
  }

  get productFormControl() {
    return this.productForm.controls;
  }
}
