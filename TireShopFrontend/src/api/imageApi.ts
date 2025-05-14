import { AddImageRequest, CreateImageRequest, Image } from "../types/Image";
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

export const createImage = async (
  request: CreateImageRequest,
): Promise<string> => {
  const response = await axiosInstance.post("/admin/image", request);
  return response.data;
};

export const addImagesToProduct = async (
  productId: number,
  request: AddImageRequest[],
): Promise<string> => {
  const response = await axiosInstance.post(
    `/admin/images/product/${productId}`,
    request,
  );
  return response.data;
};

export const updateImage = async (
  id: number,
  request: CreateImageRequest,
): Promise<string> => {
  const response = await axiosInstance.patch(`/admin/image/${id}`, request);
  return response.data;
};

export const deleteImage = async (id: number): Promise<string> => {
  const response = await axiosInstance.delete(`/admin/image/${id}`);
  return response.data;
};

export const deleteImageByProductId = async (
  productId: number,
): Promise<string> => {
  const response = await axiosInstance.delete(
    `/admin/image/products/${productId}`,
  );
  return response.data;
};
