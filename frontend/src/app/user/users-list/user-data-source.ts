import {DataSource} from '@angular/cdk/table';
import {CollectionViewer} from '@angular/cdk/collections';
import {BehaviorSubject, Observable, of} from 'rxjs';
import {catchError, finalize} from 'rxjs/operators';
import {UserService} from '../User.service';
import {Page} from '../../page';
import {UserPayload} from '../user-payload';

export class UserDataSource implements DataSource<UserPayload>{

  private userSubject = new BehaviorSubject<UserPayload[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private countSubject = new BehaviorSubject<number>(0);
  public counter$ = this.countSubject.asObservable();

  constructor(private userService: UserService) { }

  connect(collectionViewer: CollectionViewer): Observable<UserPayload[]> {
    return this.userSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.userSubject.complete();
    this.loadingSubject.complete();
    this.countSubject.complete();
  }

  loadUsers(pageNumber = 0, pageSize = 5, searchValue: string = '') {
    this.loadingSubject.next(true);
    this.userService.getAllPaginated({ page: pageNumber, size: pageSize, searchValue})
      .pipe(
        catchError(() => of([])),
        finalize(() => this.loadingSubject.next(false))
      )
      .subscribe((result: Page<UserPayload>) => {
          this.userSubject.next(result.content);
          this.countSubject.next(result.totalElements);
        }
      );
  }
}
