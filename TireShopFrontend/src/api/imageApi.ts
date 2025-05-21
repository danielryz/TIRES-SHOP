import { Image } from "../types/Image";
import axiosInstance from "./axiosInstance";

export const getImagesByProductId = async (
  productId: number,
): Promise<Image[]> => {
  const response = await axiosInstance.get(`/image/products/${productId}`);
  return response.data;
};

export const getAllImages = async (): Promise<Image[]> => {
  const response = await axiosInstance.get("/image");
  return response.data;
};

export const getImageById = async (id: number): Promise<Image> => {
  const response = await axiosInstance.get(`/image/${id}`);
  return response.data;
};

export const deleteImage = async (id: number): Promise<string> => {
  const response = await axiosInstance.delete(`/admin/image/${id}`);
  return response.data;
};

export const uploadImageForProduct = async (
  productId: number,
  file: File,
): Promise<Image> => {
  const formData = new FormData();
  formData.append("file", file);

  const response = await axiosInstance.post<Image>(
    `/admin/image/upload/${productId}`,
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    },
  );
  return response.data;
};
