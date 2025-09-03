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

export const fetchAccountAPI = async (searchPath) => {
    const response = await authorizedAxiosInstance.get(`/v1/accounts${searchPath}`);
    return response.data.data;
}

export const fetchRoleAPI = async () => {
    const response = await authorizedAxiosInstance.get(`/v1/roles`);
    return response.data.data;
}

export const fetchUserLevelAPI = async () => {
    const response = await authorizedAxiosInstance.get(`/v1/userLevels`);
    return response.data.data;
}

export const createNewAccountAPI = async (data) => {
    const response = await authorizedAxiosInstance.post(`/v1/accounts`, data);
    return response.data.data;
}

export const getDetailAccountAPI = async (accountId) => {
    const response = await authorizedAxiosInstance.get(`/v1/accounts/${accountId}`);
    return response.data.data;
}

export const updateAccountAPI = async (accountId, updateData) => {
    const response = await authorizedAxiosInstance.put(`/v1/accounts/${accountId}`, updateData);
    return response.data.data;
}

export const createNewUserLevelAPI = async (data) => {
    const response = await authorizedAxiosInstance.post(`/v1/userLevels`, data);
    return response.data.data;
}

export const fetchCardTypesAPI = async () => {
    const response = await authorizedAxiosInstance.get(`/v1/cards/types`);
    return response.data.data;
}

export const fetchCardStatusAPI = async () => {
    const response = await authorizedAxiosInstance.get(`/v1/cards/status`);
    return response.data.data;
}

export const createNewCardAPI = async (data) => {
    const response = await authorizedAxiosInstance.post(`/v1/cards`, data);
    return response.data.data;
}