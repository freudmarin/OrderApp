import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Customer} from './customer';
import {Page} from '../page';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private customersUrl = 'http://localhost:8080/api/customers';

  constructor(private httpClient: HttpClient) {
  }

  getAllPaginated(request): Observable<Page<Customer>> {
    const params = request;
    return this.httpClient.get<Page<Customer>>(this.customersUrl + '/paginated', {params});
  }

  getAll()  {
    return this.httpClient.get<Customer[]>(this.customersUrl);
  }
  addCustomer(customer): Observable<Customer> {
    return this.httpClient.post<Customer>(this.customersUrl + '/add', customer);
  }

  get(id) {
    return this.httpClient.get<Customer>(`${this.customersUrl}/${id}`);
  }

  update(id, customer: Customer) {
    return this.httpClient.put(`${this.customersUrl}/${id}`, customer);
  }

  delete(id) {
    return this.httpClient.delete(`${this.customersUrl}/${id}`);
  }
}
