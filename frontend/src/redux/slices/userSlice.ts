import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  id: null,
  name: "",
  email: "",
  wallet: {
    id: null,
    balance: 0,
    publicKey:"",
    secretKey:"",
    solPerRupee:""
  },
  initialTransactionString:"",
  meter: {
    id: null,
    energyGenerated: 0,
    energyConsumed: 0,
    lastReading: null,
  },
  buyingTransactions: [
    {
      transactionAmount: 0,
      price: 0,
      date: "",
    },
  ],
  sellingTransactions: [
    {
      transactionAmount: 0,
      price: 0,
      date: "",
    },
  ],
};

const userSlice = createSlice({
  name: "user",
  initialState: initialState,
  reducers: {
    updateUser: (state, action) => {
      return {
        ...state,
        ...action.payload,
      };
    },
    resetUser: () => {
      return {...initialState};
    },
    updateWallet: (state, action) => {
      state.wallet.balance = action.payload.balance;
    },
    updateMeter: (state, action) => {
      state.meter = {
        ...state.meter,
        ...action.payload,
      };
    },
    updateBuyingTransactions: (state, action) => {
      state.buyingTransactions = action.payload;
    },
    updateSellingTransactions: (state, action) => {
      state.sellingTransactions = action.payload;
    },
  },
});

export const {
  updateUser,
  resetUser,
  updateWallet,
  updateMeter,
  updateBuyingTransactions,
  updateSellingTransactions,
} = userSlice.actions;
export default userSlice.reducer;
