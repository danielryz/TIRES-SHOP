import {
  ProductFilterParams,
  Product,
  CreateProductRequest,
  UpdateProductRequest,
} from "../types/Product";
import { Page } from "../types/Page";
import axiosInstance from "./axiosInstance";

export const getProducts = async (
  params: ProductFilterParams,
): Promise<Page<Product>> => {
  const searchParams = new URLSearchParams();

  if (params.name) searchParams.append("name", params.name);
  if (params.minPrice != null)
    searchParams.append("minPrice", params.minPrice.toString());
  if (params.maxPrice != null)
    searchParams.append("maxPrice", params.maxPrice.toString());
  if (params.type) searchParams.append("type", params.type);

  searchParams.append("page", (params.page ?? 0).toString());
  searchParams.append("sizePerPage", (params.sizePerPage ?? 10).toString());
  searchParams.append("sort", params.sort ?? "id,asc");

  const response = await axiosInstance.get(
    `/products?${searchParams.toString()}`,
  );
  return response.data;
};

export const getProductById = async (id: number): Promise<Product> => {
  const response = await axiosInstance.get(`/product/${id}`);
  return response.data;
};

export const createProducts = async (
  products: CreateProductRequest[],
): Promise<Product[]> => {
  const response = await axiosInstance.post<Product[]>(
    "/admin/products",
    products,
  );
  return response.data;
};

export const updateProduct = async (
  id: number,
  product: UpdateProductRequest,
): Promise<string> => {
  const response = await axiosInstance.patch(`/admin/products/${id}`, product);
  return response.data;
};

export const deleteProduct = async (id: number): Promise<string> => {
  const response = await axiosInstance.delete(`/admin/products/${id}`);
  return response.data;
};
