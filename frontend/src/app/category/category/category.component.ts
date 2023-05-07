import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {CategoryService} from '../category-service';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {

  id: number;
  onUpdate: boolean;
  categoryForm: FormGroup;
  submitted = false;

  constructor(private formBuilder: FormBuilder, private categoryService: CategoryService, private router: Router,
              private route: ActivatedRoute) {
    this.categoryForm = this.formBuilder.group({
      name: ['', Validators.required],
    });
  }

  ngOnInit() {
    this.id = this.route.snapshot.params.id;
    if (this.id > 0) {
      this.onUpdate = true;
      this.categoryService.get(this.id).subscribe(data => {
        this.categoryForm.patchValue(data);
      }, error => console.log(error));
    }
  }

  onSubmit() {
    this.submitted = true;
    if (this.categoryForm.valid) {
      if (!this.id) {
        this.categoryService.addCategory(this.categoryForm.value).subscribe(data => {
          console.log('user added');
          this.router.navigateByUrl('/categories');
        });
      } else {
        this.onUpdate = true;
        this.categoryService.update(this.id, this.categoryForm.value).subscribe(data => {
          console.log('update usccess');
          this.router.navigateByUrl('/categories');
        });
      }
    }
  }

  get categoryFormControl() {
    return this.categoryForm.controls;
  }

}
