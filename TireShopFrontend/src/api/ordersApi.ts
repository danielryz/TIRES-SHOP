import axiosInstance from "./axiosInstance";
import {
  CreateOrderRequest,
  CreateShippingAddressRequest,
  OrderResponse,
} from "../types/Order";

export const createOrder = async (
  data: CreateOrderRequest,
): Promise<OrderResponse> => {
  const response = await axiosInstance.post("/api/orders/public", data);
  return response.data;
};

export const addShippingAddress = async (
  data: CreateShippingAddressRequest,
): Promise<string> => {
  const response = await axiosInstance.post(
    "/api/shippingAddress/my_order",
    data,
  );
  return response.data;
};

export const getUserOrders = async (): Promise<OrderResponse[]> => {
  const response = await axiosInstance.get("/api/orders/user");
  return response.data;
};

export const cancelOrder = async (id: number): Promise<string> => {
  const response = await axiosInstance.patch(`/api/orders/${id}/cancel`);
  return response.data;
};
