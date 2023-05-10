import {DataSource} from '@angular/cdk/table';
import {CollectionViewer} from '@angular/cdk/collections';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {catchError, finalize} from 'rxjs/operators';
import {Product} from '../product';
import {ProductService} from '../product.service';
import {Page} from '../../page';

export class ProductDataSource implements DataSource<Product>{

  private productSubject = new BehaviorSubject<Product[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private countSubject = new BehaviorSubject<number>(0);
  public counter$ = this.countSubject.asObservable();

  constructor(private productService: ProductService) { }

  connect(collectionViewer: CollectionViewer): Observable<Product[]> {
    return this.productSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.productSubject.complete();
    this.loadingSubject.complete();
    this.countSubject.complete();
  }

  loadProducts(pageNumber = 0, pageSize = 5, searchValue: string = '') {
    this.loadingSubject.next(true);
    this.productService.getAllPaginated({ page: pageNumber, size: pageSize, searchValue })
      .pipe(
        catchError(() => of([])),
        finalize(() => this.loadingSubject.next(false))
      )
      .subscribe((result: Page<Product>) => {
          this.productSubject.next(result.content);
          this.countSubject.next(result.totalElements);
        }
      );
  }
}
