import axiosInstance from "./axiosInstance";
import {
  CreateOrderRequest,
  OrderFilterParams,
  OrderResponse,
  OrderStatus,
} from "../types/Order";
import { Page } from "../types/Page";

export const createOrder = async (
  data: CreateOrderRequest,
): Promise<OrderResponse> => {
  const response = await axiosInstance.post("/orders/public", data);
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

export const getOrders = async (
  params: OrderFilterParams,
): Promise<Page<OrderResponse>> => {
  const searchParams = new URLSearchParams();

  if (params.userId != null)
    searchParams.append("userId", params.userId.toString());
  if (params.status) searchParams.append("status", params.status);
  if (params.createdAtFrom)
    searchParams.append("createdAtFrom", params.createdAtFrom);
  if (params.createdAtTo)
    searchParams.append("createdAtTo", params.createdAtTo);
  if (params.isPaid != null)
    searchParams.append("isPaid", params.isPaid.toString());
  if (params.paidAtFrom) searchParams.append("paidAtFrom", params.paidAtFrom);
  if (params.paidAtTo) searchParams.append("paidAtTo", params.paidAtTo);

  searchParams.append("page", (params.page ?? 0).toString());
  searchParams.append("sizePerPage", (params.sizePerPage ?? 10).toString());
  searchParams.append("sort", params.sort ?? "id,asc");

  const response = await axiosInstance.get(`/orders/admin?${searchParams}`);
  return response.data;
};

export const getOrderByIdAdmin = async (id: number): Promise<OrderResponse> => {
  const response = await axiosInstance.get(`/orders/admin/${id}`);
  return response.data;
};

export const updateOrderStatus = async (
  id: number,
  status: OrderStatus,
): Promise<string> => {
  const response = await axiosInstance.patch(
    `/orders/admin/${id}/status?status=${status}`,
  );
  return response.data;
};
