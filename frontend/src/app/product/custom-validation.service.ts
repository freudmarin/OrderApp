import {Injectable} from '@angular/core';
import {AbstractControl, AsyncValidatorFn, FormGroup, ValidationErrors} from '@angular/forms';
import {ProductService} from './product.service';
import {catchError, map} from 'rxjs/operators';
import {Observable, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CustomValidationService {
  constructor(private productService: ProductService) {
  }

  uniqueProductCodeValidator(): AsyncValidatorFn {
    return (control: AbstractControl): Promise<ValidationErrors | null> | Observable<ValidationErrors | null> => {
      return this.productService.isProductCodeNotUnique(control.value).pipe(
        map(isNotUnique => isNotUnique ? { productCodeNotUnique: true } : null),
        catchError(() => of(null)) // return null if an error occurs when calling `isProductCodeUnique()`
      );
    };
  }
}
