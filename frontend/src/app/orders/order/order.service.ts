import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export class OrderResponseDto {
  orderId: number;
  customerName: String;
}

export class OrderItemDto {
  price: number;
  quantity: number;
  productCode: number;
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

  getAllOrders(): Observable<OrderResponse[]> {
    return this.http.get<OrderResponse[]>(`${this.ordersUrl}/all/admin`);
  }

  getOrdersByUser(): Observable<OrderResponse[]> {
    return this.http.get<OrderResponse[]>(this.ordersUrl);
  }

  placeOrder(orderReq: OrderRequest): Observable<void> {
    return this.http.post<void>(`${this.ordersUrl}/add`, orderReq);
  }
}
