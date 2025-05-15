import { Tire } from "../types/Tire";
import axiosInstance from "./axiosInstance";

export const getTires = async (): Promise<Tire[]> => {
  const response = await axiosInstance.get("/tire");
  return response.data;
};

export const getTireById = async (id: number): Promise<Tire> => {
  const response = await axiosInstance.get(`/tire/${id}`);
  return response.data;
};

export const getTireByFilter = async (season?: string): Promise<Tire> => {
  const params = season ? { season } : {};
  const response = await axiosInstance.get(`/tire`, { params });
  return response.data;
};
