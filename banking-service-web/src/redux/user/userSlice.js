import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import authorizedAxiosInstance from '~/utils/authorizeAxios';
import { APP_ROOT } from '~/utils/constant';
import { toast } from 'react-toastify'
// khởi tạo giá trị State của 1 slice trong redux
const initialState = {
    currentUser: null
}

/**
 * Các hành động gọi API (bất đồng bộ) và cập nhật dữ liệu vào Redux, dùng middleware createAsyncThunk
 * đi kèm với extraReducers
 */

export const loginUserAPI = createAsyncThunk(
    'user/loginUserAPI',
    async (data, { rejectWithValue }) => {
        try {
            const response = await authorizedAxiosInstance.post(`/v1/auth/login`, data);
            const resData = response.data.data;

            // Lưu token ra ngoài Redux (localStorage / cookie)
            localStorage.setItem("access_token", resData.accessToken);
            localStorage.setItem("refresh_token", resData.refreshToken);

            // Tách roles và permissions
            const roles = resData.accountResponse.roles.map(r => r.name);
            const permissions = resData.accountResponse.roles.flatMap(r =>
                r.permissions.map(p => p.name)
            );

            // Trả về object đã loại bỏ roles lồng
            const { roles: _, ...accountWithoutNestedRoles } = resData.accountResponse;

            return {
                ...accountWithoutNestedRoles,
                roles,
                permissions
            };
        } catch (error) {
            return rejectWithValue(error.response?.data || error.message);
        }
    }
);



export const logoutUserAPI = createAsyncThunk(
    'user/logoutUserAPI',
    async (showSuccessMessage = true) => {
        const accessToken = localStorage.getItem("access_token");
        const refreshToken = localStorage.getItem("refresh_token");

        await authorizedAxiosInstance.post(`/v1/auth/logout`, {
            accessToken,
            refreshToken
        });

        // clear token ở localStorage
        localStorage.removeItem("access_token");
        localStorage.removeItem("refresh_token");

        if (showSuccessMessage) {
            toast.success('Logged out successfully')
        }
    }
)


// export const updateUserAPI = createAsyncThunk(
//     'account/updateUserAPI',
//     async (data) => {
//         const response = await authorizedAxiosInstance.put(`${APP_ROOT}/v1/accounts/update`, data);
//         return response.data;
//     }
// )

//Khởi tạo một cái slice trong kho lưu trữ - Redux Store
export const userSlice = createSlice({
    name: 'user',
    initialState,

    //Reducers: nơi xử lí dữ liệu đồng bộ
    //ExtraReducers: nơi xử lí dữ liệu bất đồng bộ 
    extraReducers: (builder) => {
        builder.addCase(loginUserAPI.fulfilled, (state, action) => {
            state.currentUser = action.payload
        })
        builder.addCase(logoutUserAPI.fulfilled, (state) => {
            /**
             * API logout sau khi gọi thành công thì clear thông tin currentUser về null ở đây
             * Kết hợp với ProtectRoutes đã làm ở App.js => code sẽ điều hướng về trang login
             */
            state.currentUser = null
        })
        // builder.addCase(updateUserAPI.fulfilled, (state, action) => {
        //     state.currentUser = action.payload
        // })
    }
})
/**
 * Actions: là nơi dành cho các components bên dưới gọi bằng dispatch() tới nó để cập nhật lại dữ liệu
 * thông qua reducer (chạy đồng bộ)
 * Để ý ở trên thì không thấy properties actions đâu cả, bởi vì những actions này đơn giản được thằng
 * redux tạo tự động theo tên của reducer nhé
 */

// export const { } = accountSlice.actions

/** 
 * Selectors: Là nơi dành cho các component bên dưới gọi bằng hook useSelector() để lấy dữ liệu
 * từ trong kho Redux ra để sử dụng
*/
export const selectCurrentUser = (state) => {
    return state.user.currentUser
}

export const userReducer = userSlice.reducer