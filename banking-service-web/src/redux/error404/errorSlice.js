import { createSlice } from "@reduxjs/toolkit"

const errorSlice = createSlice({
    name: "error",
    initialState: {
        status: null
    },
    reducers: {
        setGlobalError: (state, action) => {
            state.status = action.payload
        },
        clearGlobalError: (state) => {
            state.status = null
        }
    }
})

export const { setGlobalError, clearGlobalError } = errorSlice.actions
export const selectGlobalError = (state) => state.error.status
export const errorReducer = errorSlice.reducer
