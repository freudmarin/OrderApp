import {DataSource} from '@angular/cdk/table';
import {CollectionViewer} from '@angular/cdk/collections';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {catchError, finalize} from 'rxjs/operators';
import {Page} from '../../page';
import {OrderResponse, OrdersService} from './order.service';
import {RoleManagementService} from '../../role-management.service';

export class OrderDataSource implements DataSource<OrderResponse> {

  private orderSubject = new BehaviorSubject<OrderResponse[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private countSubject = new BehaviorSubject<number>(0);
  public counter$ = this.countSubject.asObservable();

  constructor(private orderService: OrdersService, private roleService: RoleManagementService) {
  }

  connect(collectionViewer: CollectionViewer): Observable<OrderResponse[]> {
    return this.orderSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.orderSubject.complete();
    this.loadingSubject.complete();
    this.countSubject.complete();
  }

  loadOrders(pageNumber = 0, pageSize = 5, searchValue: string = '') {
    this.loadingSubject.next(true);
    if (this.roleService.isAdmin) {
      this.orderService.getAllOrdersPaginated({page: pageNumber, size: pageSize, searchValue})
        .pipe(
          catchError(() => of([])),
          finalize(() => this.loadingSubject.next(false))
        )
        .subscribe((result: Page<OrderResponse>) => {
            this.orderSubject.next(result.content);
            this.countSubject.next(result.totalElements);
          }
        );
    } else {
      this.orderService.getOrdersByUser({page: pageNumber, size: pageSize, searchValue})
        .pipe(
          catchError(() => of([])),
          finalize(() => this.loadingSubject.next(false))
        )
        .subscribe((result: Page<OrderResponse>) => {
            this.orderSubject.next(result.content);
            this.countSubject.next(result.totalElements);
          }
        );
    }
  }
}
