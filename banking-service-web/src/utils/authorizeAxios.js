import axios from 'axios';
import { toast } from "react-toastify";
import { interceptorLoadingElements } from "~/utils/formatters";
import { refreshTokenAPI } from '~/apis';
import { logoutUserAPI } from "~/redux/user/userSlice";
import { setGlobalError } from '~/redux/error404/errorSlice';
import { APP_ROOT } from '~/utils/constant';

/**
 * Inject Redux store để sử dụng trong file ngoài phạm vi component.
 * Gán mainStore vào axiosReduxStore khi ứng dụng khởi động (gọi từ main.jsx).
 */
let axiosReduxStore;
export const injectStore = mainStore => {
    axiosReduxStore = mainStore;
};

/**
 * Khởi tạo Axios instance với cấu hình chung cho dự án.
 */
const authorizedAxiosInstance = axios.create({
    baseURL: APP_ROOT,
    timeout: 1000 * 60 * 10, // Timeout 10 phút
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: false,

});

/**
 * Danh sách endpoint công khai không yêu cầu access token.
 */
const publicEndpoints = [
    '/auth/login',
    '/auth/register',
    '/auth/verify',
];

/**
 * Interceptor Request: Thêm Authorization header và kích hoạt loading.
 * Bỏ qua kiểm tra token cho các endpoint công khai.
 */
authorizedAxiosInstance.interceptors.request.use(
    (config) => {
        interceptorLoadingElements(true);
        // Kiểm tra nếu request không phải endpoint công khai
        const isPublicEndpoint = publicEndpoints.some(endpoint => config.url.includes(endpoint));
        if (!isPublicEndpoint) {
            const accessToken = localStorage.getItem('access_token');
            if (accessToken) {
                config.headers.Authorization = `Bearer ${accessToken}`;
            } else {
                return Promise.reject(new Error('No access token found. Please login.'));
            }
        }
        return config;
    },
    (error) => {
        interceptorLoadingElements(false);
        return Promise.reject(error);
    }
);

/**
 * Promise để gọi API refresh token, đảm bảo chỉ gọi một lần tại một thời điểm.
 */
let refreshTokenPromise = null;

/**
 * Interceptor Response: Xử lý response và lỗi từ backend.
 * Xử lý lỗi 401 (UNAUTHENTICATED, TOKEN_EXPIRED_EXCEPTION) và 404.
 */
authorizedAxiosInstance.interceptors.response.use(
    (response) => {
        interceptorLoadingElements(false); // Tắt loading
        return response;
    },
    async (error) => {
        interceptorLoadingElements(false); // Tắt loading

        const originalRequest = error.config;

        // Xử lý lỗi HTTP 401 từ Spring Boot
        if (error.response?.status === 401 && !originalRequest._retry) {
            const errorCode = error.response?.data?.code;

            // Trường hợp 1: UNAUTHENTICATED (code: 1001) -> Logout ngay
            if (errorCode === 1001) {
                localStorage.removeItem('access_token');
                localStorage.removeItem('refresh_token');
                axiosReduxStore.dispatch(logoutUserAPI(false));
                window.location.href = '/login';
                return Promise.reject(error);
            }

            // Trường hợp 2: TOKEN_EXPIRED_EXCEPTION (code: 1002) -> Gọi refresh token
            if (errorCode === 1002) {
                originalRequest._retry = true;
                const refreshToken = localStorage.getItem('refresh_token');
                if (refreshToken) {
                    if (!refreshTokenPromise) {
                        refreshTokenPromise = refreshTokenAPI(refreshToken)
                            .then((data) => {
                                const newAccessToken = data?.accessToken;
                                if (data?.refreshToken) {
                                    localStorage.setItem('refresh_token', data.refreshToken);
                                }
                                localStorage.setItem('access_token', newAccessToken);
                                return newAccessToken;
                            })
                            .catch(() => {
                                localStorage.removeItem('access_token');
                                localStorage.removeItem('refresh_token');
                                axiosReduxStore.dispatch(logoutAccountAPI(false));
                                window.location.href = '/login';
                            })
                            .finally(() => {
                                refreshTokenPromise = null;
                            });
                    }
                    return refreshTokenPromise.then((accessToken) => {
                        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
                        return authorizedAxiosInstance(originalRequest);
                    });
                } else {
                    localStorage.removeItem('access_token');
                    axiosReduxStore.dispatch(logoutUserAPI(false));
                    window.location.href = '/login';
                    return Promise.reject(error);
                }
            }
        }

        // Xử lý lỗi 404: Điều hướng đến trang NotFound
        if (error.response?.status === 404) {
            axiosReduxStore.dispatch(setGlobalError(404));
        }

        // Hiển thị thông báo lỗi bằng toast, trừ lỗi 1002 (refresh token)
        let errorMessage = error?.message;
        if (error.response?.data?.message) {
            errorMessage = error.response.data.message;
        }
        if (error.response?.data?.code !== 1002) {
            toast.error(errorMessage);
        }

        return Promise.reject(error);
    }
);

export default authorizedAxiosInstance

