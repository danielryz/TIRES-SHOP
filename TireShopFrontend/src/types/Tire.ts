import { ProductType } from "./Product";

export interface Tire {
  id: number;
  name: string;
  price: number;
  size: string;
  season: string;
  stock: number;
  description: string;
  productType: ProductType;
  imageUrls?: string[];
}

export interface TireFilterParams {
  name?: string;
  season?: string;
  size?: string;
  minPrice?: number;
  maxPrice?: number;
  page?: number;
  sizePerPage?: number;
  sort?: string;
}

export interface CreateTireRequest {
  name: string;
  price: number;
  description: string;
  stock: number;
  type: ProductType;
  season: string;
  size: string;
}

export interface UpdateTireRequest {
  name: string;
  price: number;
  description: string;
  stock: number;
  type: ProductType;
  season: string;
  size: string;
}
