import axiosInstance from "./axiosInstance";
import { User, UserFilterParams } from "../types/User";

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

//ADMIN API FOR USER

export const getUsers = async (params: UserFilterParams) => {
  const searchParams = new URLSearchParams();

  if (params.email) searchParams.append("email", params.email);
  if (params.username) searchParams.append("username", params.username);
  if (params.firstName) searchParams.append("firstName", params.firstName);
  if (params.lastName) searchParams.append("lastName", params.lastName);
  if (params.phoneNumber)
    searchParams.append("phoneNumber", params.phoneNumber);
  if (params.role) searchParams.append("role", params.role);

  searchParams.append("page", (params.page ?? 0).toString());
  searchParams.append("sizePerPage", (params.sizePerPage ?? 10).toString());
  searchParams.append("sort", params.sort ?? "id,asc");

  const response = await axiosInstance.get(
    `/admin/users?${searchParams.toString()}`,
  );
  return response.data;
};

export const getUserById = async (id: number): Promise<User> => {
  const response = await axiosInstance.get(`/admin/users/${id}`);
  return response.data;
};

export const addRoleToUser = async (
  userId: number,
  roleId: number,
): Promise<string> => {
  const response = await axiosInstance.post(
    `/admin/users/${userId}/role/${roleId}`,
  );
  return response.data;
};

export const removeUserRole = async (
  userId: number,
  roleId: number,
): Promise<string> => {
  const response = await axiosInstance.delete(
    `/admin/users/${userId}/role/${roleId}`,
  );
  return response.data;
};

export const deleteUserById = async (id: number): Promise<string> => {
  const response = await axiosInstance.delete(`/admin/users/${id}`);
  return response.data;
};
