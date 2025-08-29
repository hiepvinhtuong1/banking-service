import authorizedAxiosInstance from '~/utils/authorizeAxios';
import { toast } from 'react-toastify';


export const registerUserAPI = async (data) => {
    const response = await authorizedAxiosInstance.post(`/v1/auth/register`, data);
    if (response.data.code === 1000) {
        toast.success('Account created successfully! Please check and verify your account before logging in');
    }
    return response.data.data; // Trả về AccountResponse
};

export const verifyAccountAPI = async (data) => {
    const response = await authorizedAxiosInstance.put(`/v1/auth/verify`, {
        email: data.email,
        code: data.code,
    });

    return response.data.data; // Trả về AccountResponse
};

export const refreshTokenAPI = async (refreshToken) => {
    const response = await authorizedAxiosInstance.post(`/v1/auth/refresh-token`, { refreshToken });
    return response.data.data; // Trả về RefreshTokenResponse { accessToken, refreshToken }
};

