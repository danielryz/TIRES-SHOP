import { Tire } from '../types/Tire';
import axiosInstance from "./axiosInstance";

export const getTires = async (): Promise<Tire[]> => {
    const response = await axiosInstance.get('/api/tire');
    return response.data;
};

