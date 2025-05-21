import { AccessoryType } from "./Accessory";

export enum ProductType {
  TIRE = "TIRE",
  RIM = "RIM",
  ACCESSORY = "ACCESSORY",
  ALL = "ALL",
}

export interface Product {
  id: number;
  name: string;
  price: number;
  description: string;
  stock: number;
  type: ProductType;
  imageUrls?: string[];
}

export interface ProductFilterParams {
  name?: string;
  minPrice?: number;
  maxPrice?: number;
  type?: ProductType;
  page?: number;
  sizePerPage?: number;
  sort?: string;
}

export interface CreateProductRequest {
  name: string;
  price: number;
  description: string;
  stock: number;
  type: ProductType;
}

export interface UpdateProductRequest {
  name: string;
  price: number;
  description: string;
  stock: number;
  type: ProductType;
}

export interface ProductResponse {
  id: number;
  name: string;
  price: number;
  description: string;
  stock: number;
  type: ProductType;
  imageUrls?: string[];
}

export interface TireResponse extends ProductResponse {
  type: ProductType.TIRE;
  season: string;
  size: string;
}

export interface RimResponse extends ProductResponse {
  type: ProductType.RIM;
  material: string;
  size: string;
  boltPattern: string;
}

export interface AccessoryResponse extends ProductResponse {
  type: ProductType.ACCESSORY;
  accessoryType: AccessoryType;
}

export type ProductApiResponse =
  | ProductResponse
  | TireResponse
  | RimResponse
  | AccessoryResponse;

export interface ProductSearchParams {
  query?: string;
  page?: number;
  sizePerPage?: number;
  sort?: string;
}
