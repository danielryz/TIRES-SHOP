import axiosInstance from "./axiosInstance";
import {
  CreateOrderRequest,
  CreateShippingAddressRequest,
  OrderResponse,
} from "../types/Order";

export const createOrder = async (
  data: CreateOrderRequest,
): Promise<OrderResponse> => {
  const response = await axiosInstance.post("/orders/public", data);
  return response.data;
};

export const addShippingAddress = async (
  data: CreateShippingAddressRequest,
): Promise<string> => {
  const response = await axiosInstance.post("/shippingAddress/my_order", data);
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
