import {Address, AddressType} from "../types/Address";
import axiosInstance from "./axiosInstance";

export const getAddresses = async (): Promise<Address[]> => {
    const response = await axiosInstance.get('/api/address');
    return response.data;
}

export const getAddressById = async (id: number): Promise<Address> => {
    const response = await axiosInstance.get(`/api/address/${id}`);
    return response.data;
}
export const getAddressByType = async (type: AddressType): Promise<Address[]> => {
    const response = await axiosInstance.get(`/api/address/type?type=${type}`);
    return response.data;
}

export const addAddress = async (address: Omit<Address, 'id'>): Promise<string> => {
    const response = await axiosInstance.post('/api/address', address);
    return response.data || 'Adres został dodany';
};

export const updateAddress = async (id: number, address: Omit<Address, 'id'>): Promise<string> => {
    const response = await axiosInstance.patch(`/api/address/${id}`, address);
    return response.data || 'Adres został zaktualizowany';
};

export const deleteAddress = async (id: number): Promise<string> => {
    const response = await axiosInstance.delete(`/api/address/${id}`);
    return response.data || 'Adres został usunięty';
}