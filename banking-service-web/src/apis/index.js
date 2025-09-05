import authorizedAxiosInstance from '~/utils/authorizeAxios';
import { toast } from 'react-toastify';

// ==============================
// Authentication APIs
// ==============================
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

// ==============================
// Account Management APIs
// ==============================
export const fetchAccountAPI = async (searchPath) => {
    const response = await authorizedAxiosInstance.get(`/v1/accounts${searchPath}`);
    return response.data.data;
};

export const createNewAccountAPI = async (data) => {
    const response = await authorizedAxiosInstance.post(`/v1/accounts`, data);
    return response.data.data;
};

export const getDetailAccountAPI = async (accountId) => {
    const response = await authorizedAxiosInstance.get(`/v1/accounts/${accountId}`);
    return response.data.data;
};

export const updateAccountAPI = async (accountId, updateData) => {
    const response = await authorizedAxiosInstance.put(`/v1/accounts/${accountId}`, updateData);
    return response.data.data;
};

// ==============================
// Role Management APIs
// ==============================
export const fetchRoleAPI = async () => {
    const response = await authorizedAxiosInstance.get(`/v1/roles`);
    return response.data.data;
};

// ==============================
// User Level Management APIs
// ==============================
export const fetchUserLevelAPI = async () => {
    const response = await authorizedAxiosInstance.get(`/v1/userLevels`);
    return response.data.data;
};

export const createNewUserLevelAPI = async (data) => {
    const response = await authorizedAxiosInstance.post(`/v1/userLevels`, data);
    return response.data.data;
};

// ==============================
// Card Management APIs
// ==============================
export const fetchCardTypesAPI = async () => {
    const response = await authorizedAxiosInstance.get(`/v1/cards/types`);
    return response.data.data;
};

export const fetchCardStatusAPI = async () => {
    const response = await authorizedAxiosInstance.get(`/v1/cards/status`);
    return response.data.data;
};

export const createNewCardAPI = async (data) => {
    const response = await authorizedAxiosInstance.post(`/v1/cards`, data);
    return response.data.data;
};

export const fetchCardsAPI = async (searchPath) => {
    const response = await authorizedAxiosInstance.get(`/v1/cards${searchPath}`);
    return response.data.data;
};

export const fetchCardByIdAPI = async (cardId) => {
    const response = await authorizedAxiosInstance.get(`/v1/cards/${cardId}`);
    return response.data.data;
};

export const fetchCardByCardNumberAPI = async (cardNumber) => {
    const response = await authorizedAxiosInstance.get(`/v1/cards/card-number/${cardNumber}`);
    return response.data.data;
};

// ==============================
// Balance Management APIs
// ==============================
export const fetchBalanceByAccountIdAPI = async (accountId) => {
    const response = await authorizedAxiosInstance.get(`/v1/balances/${accountId}`);
    return response.data.data;
};

// ==============================
// OTP Management APIs
// ==============================
export const sendOtpAPI = async (email) => {
    const response = await authorizedAxiosInstance.post(`/v1/otp/send?email=${email}`);
    return response.data.data;
};

export const verifyOtpAPI = async (data) => {
    const response = await authorizedAxiosInstance.post(`/v1/otp/verify`, data);
    return response.data.data;
};

// ==============================
// Payment Management APIs
// ==============================
export const createPaymentAPI = async (data) => {
    const response = await authorizedAxiosInstance.post(`/v1/payments`, data);
    return response.data.data; // Trả về TransactionResponse
};

export const depositAPI = async () => {
    // TODO: Implement deposit logic
};

export const withdrawAPI = async () => {
    // TODO: Implement withdraw logic
};

// ==============================
// Transaction Management APIs
// ==============================
export const fetchTransactionsAPI = async (searchPath) => {
    const response = await authorizedAxiosInstance.get(`/v1/transactions${searchPath}`);
    return response.data.data;
};

export const fetchTransactionTypesAPI = async () => {
    const response = await authorizedAxiosInstance.get(`/v1/transactions/types`);
    return response.data.data;
};

export const fetchTransactionStatusAPI = async () => {
    const response = await authorizedAxiosInstance.get(`/v1/transactions/status`);
    return response.data.data;
};