import { createSlice, createAsyncThunk } from '@reduxjs/toolkit'
import authorizedAxiosInstance from '~/utils/authorizeAxios';

// khởi tạo giá trị State của 1 slice trong redux
const initialState = {
    currentActiveAccount: null
}

/**
 * Các hành động gọi API (bất đồng bộ) và cập nhật dữ liệu vào Redux, dùng middleware createAsyncThunk
 * đi kèm với extraReducers
 */

export const fetchAccountDetailsAPI = createAsyncThunk(
    'activeAccount/fetchAccountDetailsAPI',
    async (accountId) => {
        const response = await authorizedAxiosInstance.get(`/v1/accounts/${accountId}`);
        console.log("🚀 ~ response:", response)

        //Lưu ý axios trả về kết quả về qua property của nó là data
        return response.data.data;
    }
)

//Khởi tạo một cái slice trong kho lưu trữ - Redux Store
export const activeAccountSlice = createSlice({
    name: 'activeAccount',
    initialState,

    //Reducers: nơi xử lí dữ liệu đồng bộ
    reducers: {
        updateCurrentActiveAccount: (state, action) => {
            //action.payload là chuẩn đặt tên nhận dữ liệu vào reducer, ở đây chúng ta gắn nó vào một biến có nghĩa hơn
            let account = action.payload

            //Xử lí dữ liệu nếu cần thiết ...

            //Update lại dữ liệu của currentActiveAccount
            state.currentActiveAccount = account
        },
        // updateCardInBoard: (state, action) => {
        //     const updatedCard = action.payload;

        //     //Cập nhật lại card trong currentActiveAccount
        //     if (state.currentActiveAccount) {
        //         const column = state.currentActiveAccount.columns.find(
        //             (col) => col._id === updatedCard.columnId
        //         );

        //         if (column) {
        //             const card = column.cards.find(
        //                 (c) => c._id === updatedCard._id)

        //             if (card) {

        //                 /**
        //                  * Đơn giản là dùng Object.keys() để lấy ra tất cả các keys của object updatedCard
        //                  * rồi dùng forEach để duyệt qua tất cả các key đó và gán giá trị tương ứng vào card
        //                  * trong board hiện tại
        //                  */
        //                 Object.keys(updatedCard).forEach((key) => {
        //                     card[key] = updatedCard[key];
        //                 })
        //             }
        //         }
        //     }
        // }
    },
    //ExtraReducers: nơi xử lí dữ liệu bất đồng bộ 
    extraReducers: (builder) => {
        builder.addCase(fetchAccountDetailsAPI.fulfilled, (state, action) => {

            // action.payload chính là  cái respone.data trả về ở trên
            let account = action.payload
            console.log("🚀 ~ account:", account)

            //Xử lí dữ liệu nếu cần thiết ...


            //Update lại dữ liệu của currentActiveAccount
            state.currentActiveAccount = account

        })
    }
})
/**
 * Actions: là nơi dành cho các components bên dưới gọi bằng dispatch() tới nó để cập nhật lại dữ liệu
 * thông qua reducer (chạy đồng bộ)
 * Để ý ở trên thì không thấy properties actions đâu cả, bởi vì những actions này đơn giản được thằng
 * redux tạo tự động theo tên của reducer nhé
 */

export const { updateCurrentActiveBoard } = activeAccountSlice.actions

/** 
 * Selectors: Là nơi dành cho các component bên dưới gọi bằng hook useSelector() để lấy dữ liệu
 * từ trong kho Redux ra để sử dụng
*/
export const selectCurrentActiveAccount = (state) => {
    return state.activeAccount.currentActiveAccount
}

export const activeAccountReducer = activeAccountSlice.reducer