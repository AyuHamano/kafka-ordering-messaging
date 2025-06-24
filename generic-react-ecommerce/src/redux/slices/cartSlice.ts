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
        updateQuantity(state, action) {
            const { id, quantity } = action.payload;
            const itemToUpdate = state.items.find(item => item.id === id);
            
            if (itemToUpdate) {
                // Calculate the difference in total value
                const priceDifference = itemToUpdate.price * (quantity - itemToUpdate.quantity);
                
                // Update the quantity
                itemToUpdate.quantity = quantity;
                console.log(priceDifference)
                // Update the total value
                state.totalValue += priceDifference;
            }
        },
    },
});

export const { addToCart, removeFromCart, clearCart, clearTotalValue, updateQuantity } = cartSlice.actions;
export default cartSlice.reducer;
