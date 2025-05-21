import {
  CreateTireRequest,
  Tire,
  TireFilterParams,
  TireFilters,
  UpdateTireRequest,
} from "../types/Tire";
import { Page } from "../types/Page";
import axiosInstance from "./axiosInstance";

export const getTireById = async (id: number): Promise<Tire> => {
  const response = await axiosInstance.get(`/tire/${id}`);
  return response.data;
};

export const getTire = async (
  params: TireFilterParams,
): Promise<Page<Tire>> => {
  const searchParams = new URLSearchParams();

  if (params.name) searchParams.append("name", params.name);
  if (params.season) {
    params.season.forEach((season) => searchParams.append("season", season));
  }

  if (params.size) {
    params.size.forEach((size) => searchParams.append("size", size));
  }
  if (params.minPrice != null)
    searchParams.append("minPrice", params.minPrice.toString());
  if (params.maxPrice != null)
    searchParams.append("maxPrice", params.maxPrice.toString());

  searchParams.append("page", (params.page ?? 0).toString());
  searchParams.append("sizePerPage", (params.sizePerPage ?? 10).toString());
  searchParams.append("sort", params.sort ?? "id,asc");

  const response = await axiosInstance.get(`/tires?${searchParams.toString()}`);
  return response.data;
};

export const createTires = async (
  tires: CreateTireRequest[],
): Promise<Tire[]> => {
  const response = await axiosInstance.post<Tire[]>("/admin/tire", tires);
  return response.data;
};

export const updateTire = async (
  id: number,
  tire: UpdateTireRequest,
): Promise<string> => {
  const response = await axiosInstance.patch(`/admin/tire/${id}`, tire);
  return response.data;
};

export const deleteTire = async (id: number): Promise<string> => {
  const response = await axiosInstance.delete(`/admin/tire/${id}`);
  return response.data;
};

export const getAvailableTireFilters = async (): Promise<TireFilters> => {
  const response = await axiosInstance.get("/tire/filters");
  return response.data;
};
