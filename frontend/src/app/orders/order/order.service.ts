import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Page} from '../../page';

export class OrderResponseDto {
  orderId: number;
  customerName: string;
}

export class OrderItemDto {
  price: number;
  quantity: number;
  productCode: string;
}

export class OrderRequest {
  customerId: number;
  items: OrderItemDto[];
}

export class OrderResponse {
  order: OrderResponseDto;
  items: OrderItemDto[];
}


@Injectable({
  providedIn: 'root',
})
export class OrdersService {
  private ordersUrl = 'http://localhost:8080/api/orders';

  constructor(private http: HttpClient) {}

  getAllOrdersPaginated(request): Observable<Page<OrderResponse>> {
    const params = request;
    return this.http.get<Page<OrderResponse>>(`${this.ordersUrl}/admin/paginated`, {params});
  }

  getOrdersByUser(request): Observable<Page<OrderResponse>> {
    const params = request;
    return this.http.get<Page<OrderResponse>>(this.ordersUrl, {params});
  }

  placeOrder(orderReq: OrderRequest): Observable<void> {
    return this.http.post<void>(`${this.ordersUrl}/add`, orderReq);
  }
}
