import {
  CreateRimRequest,
  Rim,
  RimFilterParams,
  UpdateRimRequest,
} from "../types/Rim";
import axiosInstance from "./axiosInstance";
import { Page } from "../types/Page";

export const getRim = async (): Promise<Rim[]> => {
  const response = await axiosInstance.get("/rim");
  return response.data;
};

export const getRimById = async (id: number): Promise<Rim> => {
  const response = await axiosInstance.get(`/rim/${id}`);
  return response.data;
};

export const getRims = async (params: RimFilterParams): Promise<Page<Rim>> => {
  const searchParams = new URLSearchParams();

  if (params.name) searchParams.append("name", params.name);
  if (params.material) searchParams.append("material", params.material);
  if (params.size) searchParams.append("size", params.size);
  if (params.boltPattern)
    searchParams.append("boltPattern", params.boltPattern);
  if (params.minPrice != null)
    searchParams.append("minPrice", params.minPrice.toString());
  if (params.maxPrice != null)
    searchParams.append("maxPrice", params.maxPrice.toString());

  searchParams.append("page", (params.page ?? 0).toString());
  searchParams.append("sizePerPage", (params.sizePerPage ?? 10).toString());
  searchParams.append("sort", params.sort ?? "id,asc");

  const response = await axiosInstance.get(`/rims?${searchParams.toString()}`);
  return response.data;
};

export const createRims = async (rims: CreateRimRequest[]): Promise<Rim[]> => {
  const response = await axiosInstance.post<Rim[]>("/admin/rim", rims);
  return response.data;
};

export const updateRim = async (
  id: number,
  rim: UpdateRimRequest,
): Promise<string> => {
  const response = await axiosInstance.patch(`/admin/rim/${id}`, rim);
  return response.data;
};

export const deleteRim = async (id: number): Promise<string> => {
  const response = await axiosInstance.delete(`/admin/rim/${id}`);
  return response.data;
};
