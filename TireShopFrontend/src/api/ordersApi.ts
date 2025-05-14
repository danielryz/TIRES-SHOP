import axiosInstance from "./axiosInstance";
import {
  CreateOrderRequest,
  CreateShippingAddressRequest,
  OrderResponse,
  OrderStatus,
  UpdateOrderStatusRequest,
} from "../types/Order";

export const createOrder = async (
  data: CreateOrderRequest,
): Promise<OrderResponse> => {
  const response = await axiosInstance.post("/orders/public", data);
  return response.data;
};

export const addShippingAddressToMyOrder = async (
  orderId: number,
  data: CreateShippingAddressRequest,
): Promise<string> => {
  const response = await axiosInstance.post(
    `/shippingAddress/my_order/${orderId}`,
    data,
  );
  return response.data;
};

export const getUserOrders = async (): Promise<OrderResponse[]> => {
  const response = await axiosInstance.get("/orders/user");
  return response.data;
};

export const cancelOrder = async (id: number): Promise<string> => {
  const response = await axiosInstance.patch(`/orders/${id}/cancel`);
  return response.data;
};

export const payForYourOrder = async (orderId: number): Promise<string> => {
  const response = await axiosInstance.patch(`/orders/public/${orderId}/pay`);
  return response.data;
};

export const getUserOrderById = async (
  orderId: number,
): Promise<OrderResponse> => {
  const response = await axiosInstance.get(`/orders/public/user/${orderId}`);
  return response.data;
};

//ADMIN API FOR ORDER

export const getAllOrdersByStatus = async (
  orderStatus?: OrderStatus,
): Promise<OrderResponse[]> => {
  const params = orderStatus ? { orderStatus } : {};
  const response = await axiosInstance.get(`/orders/admin`, { params });
  return response.data;
};

export const getOrderByIdAdmin = async (id: number): Promise<OrderResponse> => {
  const response = await axiosInstance.get(`/orders/admin/${id}`);
  return response.data;
};

export const updateOrderStatus = async (
  id: number,
  request: UpdateOrderStatusRequest,
): Promise<string> => {
  const response = await axiosInstance.patch(`/admin/${id}/status`, request);
  return response.data;
};
