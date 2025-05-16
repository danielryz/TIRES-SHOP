export interface User {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  roles: string[];
}

export interface UserFilterParams {
  email?: string;
  username?: string;
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  role?: string;
  page?: number;
  sizePerPage?: number;
  sort?: string;
}

export interface Role {
  id: number;
  name: string;
}
