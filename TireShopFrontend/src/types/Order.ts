export type OrderStatus =
  | "CREATED"
  | "CONFIRMED"
  | "IN_PROGRESS"
  | "COMPLETED"
  | "CANCELLED";

export interface OrderItemRequest {
  productId: number;
  quantity: number;
}

export interface CreateOrderRequest {
  guestFirstName: string;
  guestLastName: string;
  guestEmail: string;
  guestPhoneNumber: string;
  items: OrderItemRequest[];
}

export interface CreateShippingAddressRequest {
  street: string;
  houseNumber: string;
  apartmentNumber?: string;
  postalCode: string;
  city: string;
}

export interface OrderItemResponse {
  id: number;
  productName: string;
  quantity: number;
  priceAtPurchase: number;
  totalPrice: number;
}

export interface OrderResponse {
  id: number;
  status: string;
  totalAmount: number;
  items: OrderItemResponse[];
  createdAt: string;
  guestEmail: string;
  guestFirstName: string;
  guestLastName: string;
  isPaid: boolean;
  paidAt: string;
}
export interface UpdateOrderStatusRequest {
  status: OrderStatus;
}
