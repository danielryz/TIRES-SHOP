import axiosInstance from "./axiosInstance";
import { User } from "../types/User";

export const getUserProfile = async (): Promise<User> => {
  const response = await axiosInstance.get("/users/me");
  return response.data;
};

export const updateUserData = async (data: {
  firstName: string;
  lastName: string;
  phoneNumber: string;
  username: string;
}): Promise<string> => {
  const response = await axiosInstance.patch("/users/me/update", data);
  return response.data || "Dane użytkownika zostały zmienione.";
};

export const changeUserPassword = async (data: {
  password: string;
  newPassword: string;
}): Promise<string> => {
  const response = await axiosInstance.patch("/users/me/change-password", data);
  return response.data || "Hasło zostało zmienione.";
};

export const deleteUserData = async (): Promise<string> => {
  const response = await axiosInstance.delete("/users/me/delete-user-details");
  return response.data || "Dane użytkownika zostały usunięte.";
};

export const deleteUser = async (): Promise<string> => {
  const response = await axiosInstance.delete("/users/me/delete");
  return response.data || "Konto zostało usunięte.";
};
