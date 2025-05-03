import { Image } from "../types/Image";
import axiosInstance from "./axiosInstance";

export const getImagesByProductId = async (
  productId: number,
): Promise<Image[]> => {
  const response = await axiosInstance.get(`/image/products/${productId}`);
  return response.data;
};
