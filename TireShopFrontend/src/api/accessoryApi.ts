import {
  Accessory,
  AccessoryFilterParams,
  AccessoryFilters,
  CreateAccessoryRequest,
  UpdateAccessoryRequest,
} from "../types/Accessory";
import axiosInstance from "./axiosInstance";
import { Page } from "../types/Page";

export const getAccessoryById = async (id: number): Promise<Accessory> => {
  const response = await axiosInstance.get(`/accessory/${id}`);
  return response.data;
};

export const getAccessories = async (
  params: AccessoryFilterParams,
): Promise<Page<Accessory>> => {
  const searchParams = new URLSearchParams();

  if (params.name) searchParams.append("name", params.name);
  if (params.accessoryType) {
    params.accessoryType.forEach((accessoryType) =>
      searchParams.append("accessoryType", accessoryType),
    );
  }
  if (params.minPrice != null)
    searchParams.append("minPrice", params.minPrice.toString());
  if (params.maxPrice != null)
    searchParams.append("maxPrice", params.maxPrice.toString());

  searchParams.append("page", (params.page ?? 0).toString());
  searchParams.append("sizePerPage", (params.sizePerPage ?? 10).toString());
  searchParams.append("sort", params.sort ?? "id,asc");

  const response = await axiosInstance.get(
    `/accessories?${searchParams.toString()}`,
  );
  return response.data;
};

export const createAccessories = async (
  accessories: CreateAccessoryRequest[],
): Promise<Accessory[]> => {
  const response = await axiosInstance.post<Accessory[]>(
    "/admin/accessory",
    accessories,
  );
  return response.data;
};

export const updateAccessory = async (
  id: number,
  accessory: UpdateAccessoryRequest,
): Promise<string> => {
  const response = await axiosInstance.patch(
    `/admin/accessory/${id}`,
    accessory,
  );
  return response.data;
};

export const deleteAccessory = async (id: number): Promise<string> => {
  const response = await axiosInstance.delete(`/admin/accessory/${id}`);
  return response.data;
};

export const getAvailableAccessoryFilters =
  async (): Promise<AccessoryFilters> => {
    const response = await axiosInstance.get("/accessory/filters");
    return response.data;
  };
