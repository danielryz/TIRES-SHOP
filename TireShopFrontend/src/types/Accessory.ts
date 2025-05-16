import { ProductType } from "./Product";

export enum AccessoryType {
  SENSOR = "SENSOR",
  BOLT = "BOLT",
  CHAINS = "CHAINS",
  JACK = "JACK",
  TOOLS = "TOOLS",
}

export interface Accessory {
  id: number;
  name: string;
  price: number;
  stock: number;
  description: string;
  productType: ProductType;
  accessoryType: AccessoryType;
  imageUrls?: string[];
}

export interface AccessoryFilterParams {
  name?: string;
  accessoryType?: AccessoryType;
  minPrice?: number;
  maxPrice?: number;
  page?: number;
  sizePerPage?: number;
  sort?: string;
}

export interface CreateAccessoryRequest {
  name: string;
  price: number;
  stock: number;
  description: string;
  productType: ProductType;
  accessoryType: AccessoryType;
}

export interface UpdateAccessoryRequest {
  name: string;
  price: number;
  stock: number;
  description: string;
  productType: ProductType;
  accessoryType: AccessoryType;
}
