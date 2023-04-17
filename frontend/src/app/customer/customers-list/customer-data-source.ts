import {DataSource} from '@angular/cdk/table';
import {CollectionViewer} from '@angular/cdk/collections';
import {BehaviorSubject, Observable, of} from "rxjs";
import {catchError, finalize} from "rxjs/operators";
import {Customer} from "../customer";
import {CustomerService} from "../customer.service";
import {Page} from "../../page";

export class CustomerDataSource implements DataSource<Customer>{

  private customerSubject = new BehaviorSubject<Customer[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private countSubject = new BehaviorSubject<number>(0);
  public counter$ = this.countSubject.asObservable();

  constructor(private customerService : CustomerService) { }

  connect(collectionViewer: CollectionViewer): Observable<Customer[]> {
    return this.customerSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.customerSubject.complete();
    this.loadingSubject.complete();
    this.countSubject.complete();
  }

  loadCustomers(pageNumber = 0, pageSize = 10) {
    this.loadingSubject.next(true);
    this.customerService.getAllPaginated({ page: pageNumber, size: pageSize })
      .pipe(
        catchError(() => of([])),
        finalize(() => this.loadingSubject.next(false))
      )
      .subscribe((result: Page<Customer>) => {
          this.customerSubject.next(result.content);
          this.countSubject.next(result.totalElements);
        }
      );
  }
}
