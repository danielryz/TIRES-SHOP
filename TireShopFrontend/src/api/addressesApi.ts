import { Address, AddressType } from "../types/Address";
import axiosInstance from "./axiosInstance";

export const getAddresses = async (): Promise<Address[]> => {
  const response = await axiosInstance.get("/address");
  return response.data;
};

export const getAddressById = async (id: number): Promise<Address> => {
  const response = await axiosInstance.get(`/address/${id}`);
  return response.data;
};
export const getAddressByType = async (
  type: AddressType,
): Promise<Address[]> => {
  const response = await axiosInstance.get(`/address/type?type=${type}`);
  return response.data;
};

export const addAddress = async (
  address: Omit<Address, "id">,
): Promise<string> => {
  const response = await axiosInstance.post("/address", address);
  return response.data || "Adres został dodany";
};

export const updateAddress = async (
  id: number,
  address: Omit<Address, "id">,
): Promise<string> => {
  const response = await axiosInstance.patch(`/address/${id}`, address);
  return response.data || "Adres został zaktualizowany";
};

export const deleteAddress = async (id: number): Promise<string> => {
  const response = await axiosInstance.delete(`/address/${id}`);
  return response.data || "Adres został usunięty";
};
