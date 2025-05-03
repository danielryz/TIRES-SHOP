import { Tire } from "../types/Tire";
import axiosInstance from "./axiosInstance";

export const getTires = async (): Promise<Tire[]> => {
  const response = await axiosInstance.get("/tire");
  return response.data;
};
