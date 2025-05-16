export interface User {
  id: number;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
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
  sort?: string[];
}
