import {Image} from "../types/Image";
import axiosInstance from "./axiosInstance";


export const getImagesByProductId = async (productId: number): Promise<Image[]> => {
    const response = await axiosInstance.get(`/api/image/products/${productId}`);
    return response.data;
};