import { Accessory } from "../types/Accessory";
import axiosInstance from "./axiosInstance";

export const getAccessory = async (): Promise<Accessory[]> => {
  const response = await axiosInstance.get("/accessory");
  return response.data;
};
