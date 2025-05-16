import { ProductType } from "./Product";

export interface Rim {
  id: number;
  name: string;
  price: number;
  stock: number;
  description: string;
  productType: ProductType;
  size: string;
  material: string;
  boltPattern: string;
  imageUrls?: string[];
}

export interface RimFilterParams {
  name?: string;
  material?: string;
  size?: string;
  boltPattern?: string;
  minPrice?: number;
  maxPrice?: number;
  page?: number;
  sizePerPage?: number;
  sort?: string[];
}

export interface CreateRimRequest {
  name: string;
  price: number;
  stock: number;
  description: string;
  type: ProductType;
  material: string;
  size: string;
  boltPattern: string;
}

export interface UpdateRimRequest {
  name: string;
  price: number;
  stock: number;
  description: string;
  type: ProductType;
  material: string;
  size: string;
  boltPattern: string;
}
