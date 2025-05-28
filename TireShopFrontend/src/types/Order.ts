import { User } from "./User";

export enum OrderStatus {
  CREATED = "CREATED",
  CONFIRMED = "CONFIRMED",
  IN_PROGRESS = "IN_PROGRESS",
  COMPLETED = "COMPLETED",
  CANCELLED = "CANCELLED",
}

export interface OrderItemRequest {
  productId: number;
  quantity: number;
}

export interface CreateOrderRequest {
  guestFirstName: string;
  guestLastName: string;
  guestEmail: string;
  guestPhoneNumber: string;
  street: string;
  houseNumber: string;
  apartmentNumber?: string;
  postalCode: string;
  city: string;
  items: OrderItemRequest[];
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
  user: User;
  clientId?: string;
  shippingAddress: {
    id: number;
    street: string;
    houseNumber: string;
    apartmentNumber?: string;
    postalCode: string;
    city: string;
  };
  items: OrderItemResponse[];
  createdAt: string;
  questEmail: string;
  questFirstName: string;
  questLastName: string;
  questPhoneNumber: string;
  isPaid: boolean;
  paidAt?: string;
}
export interface OrderFilterParams {
  userId?: number;
  status?: OrderStatus;
  createdAtFrom?: string;
  createdAtTo?: string;
  isPaid?: boolean;
  paidAtFrom?: string;
  paidAtTo?: string;
  page?: number;
  sizePerPage?: number;
  sort?: string;
}
