import axiosInstance from './axiosInstance';

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
}

export const createOrder = async (data: CreateOrderRequest): Promise<OrderResponse> => {
    const response = await axiosInstance.post('/api/orders/public', data);
    return response.data;
};

export const addShippingAddress = async (data: CreateShippingAddressRequest): Promise<string> => {
    const response = await axiosInstance.post('/api/shippingAddress/my_order', data);
    return response.data;
};
