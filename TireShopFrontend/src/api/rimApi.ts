import { Rim } from "../types/Rim";
import axiosInstance from "./axiosInstance";

export const getRim = async (): Promise<Rim[]> => {
  const response = await axiosInstance.get("/rim");
  return response.data;
};
