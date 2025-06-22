import { createSlice } from '@reduxjs/toolkit';
import {ProductType} from "../../types/ProductType.ts";

const cartSlice = createSlice({
    name: 'cart',
    initialState: {
        items: [] as ProductType[],
        totalValue: 0,
        totalDiscount: 0
    },
    reducers: {
        addToCart(state, action) {
            const itemExists = state.items.find(item => item.id === action.payload.id);
            if (itemExists) {
                itemExists.quantity += action.payload.quantity;
            } else {
                state.items.push({ ...action.payload, quantity: 1 });
            }

            state.totalValue += action.payload.price;
            state.totalDiscount += action.payload.discountPercentage;
        },
        removeFromCart(state, action) {
            state.items = state.items.filter(item => item.id !== action.payload);
        },
        clearCart(state) {
            state.items = [];
            state.totalValue = 0
        },
        clearTotalValue(state, action) {
            state.totalValue -= action.payload;
        },
    },
});

export const { addToCart, removeFromCart, clearCart, clearTotalValue } = cartSlice.actions;
export default cartSlice.reducer;
