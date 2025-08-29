import { configureStore } from '@reduxjs/toolkit'
import { errorReducer } from './error404/errorSlice'
import { userReducer } from './user/userSlice'

/**
 * Cấu hình redux-persist
 */
import { combineReducers } from '@reduxjs/toolkit'
import { persistReducer } from 'redux-persist'
import storage from 'redux-persist/lib/storage'

// Cấu hình persist
const rootPersistConfig = {
    key: 'root',// key của cái persist do chúng ta chỉ định, cứ để mặc định là root
    storage: storage, // biến storage ở trên - lưu vào localstorage
    whitelist: ['user'] // định nghĩa các slice dữ liệu ĐƯỢC PHÉP duy trì qua mỗi lần f5 trình duyệt
    // blacklist: // định nghĩa các slice dữ liệu  KHÔNG ĐƯỢC PHÉP duy trì qua mỗi lần f5 trình duyệt 
}

// Combine các reducers trong dự án của chúng ta ở đây
const reducers = combineReducers({
    error: errorReducer,
    user: userReducer,
})

//Thực hiện persit Reducer
const persistedReducers = persistReducer(rootPersistConfig, reducers)

export const store = configureStore({
    reducer: persistedReducers,

    middleware: (getDefaultMiddleware) => getDefaultMiddleware({ serializableCheck: false })
})