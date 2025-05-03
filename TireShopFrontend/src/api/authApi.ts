import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

interface RegisterData {
    username: string;
    email: string;
    password: string;
}

interface LoginData {
    email: string;
    password: string;
}

interface AuthResponse {
    token: string;
}

export const registerUser = async (data: RegisterData): Promise<AuthResponse> => {
    const response = await axios.post(`${API_URL}/register`, data);
    return response.data;
};

export const loginUser = async (data: LoginData): Promise<AuthResponse> => {
    const response = await axios.post(`${API_URL}/login`, data);
    return response.data;
};
