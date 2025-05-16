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
