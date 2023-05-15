import {DataSource} from '@angular/cdk/table';
import {CollectionViewer} from '@angular/cdk/collections';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {catchError, finalize} from 'rxjs/operators';
import {Category} from '../category';
import {Page} from '../../page';
import {CategoryService} from '../category-service';

export class CategoryDataSource implements DataSource<Category>{

  private categorySubject = new BehaviorSubject<Category[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private countSubject = new BehaviorSubject<number>(0);
  public counter$ = this.countSubject.asObservable();

  constructor(private categoryService: CategoryService) { }

  connect(collectionViewer: CollectionViewer): Observable<Category[]> {
    return this.categorySubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.categorySubject.complete();
    this.loadingSubject.complete();
    this.countSubject.complete();
  }

  loadCategories(pageNumber = 0, pageSize = 10, searchValue: string = '') {
    this.loadingSubject.next(true);
    this.categoryService.getAllPaginated({ page: pageNumber, size: pageSize, searchValue })
      .pipe(
        catchError(() => of([])),
        finalize(() => this.loadingSubject.next(false))
      )
      .subscribe((result: Page<Category>) => {
          this.categorySubject.next(result.content);
          this.countSubject.next(result.totalElements);
        }
      );
  }
}
