// src/app/store.js
import { configureStore } from '@reduxjs/toolkit';
import searchSlice from "./slices/searchSlice.ts";
import cartSlice from "./slices/cartSlice.ts";

export const store = configureStore({
    reducer: {
        search: searchSlice,
        cart: cartSlice,
    },
});
