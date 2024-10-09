import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  gridData: {
    id: null,
    location: "",
    pincode: null,
    status: "",
    buyPricePerUnit: 3.0,
    sellPricePerUnit: 5.5,
    buyPricePerUnitNR: 4.0,
    reference: "",
    capacity: null,
    remainingCapacity: null,
    adminUserId: null,
  },
  tradedEnergy: [
    {
      date: "",
      totalEnergyTraded: null,
      totalTransactions: null,
      totalPrice: null,
      avgPricePerUnit: null,
      gridId: null,
    },
  ],
  peakGeneration: [
    {
      date: "",
      numberOfRequests: null,
      totalEnergyUnits: null,
      averagePricePerUnit: null,
      hour: null,
      maxEnergyGeneration: null,
      maxRequests: null,
    },
  ],
  peakDemand: [
    {
      date: "",
      numberOfRequests: null,
      totalEnergyUnits: null,
      hour: null,
      totalRequests: null,
      maxEnergyDemand: null,
      maxRequests: null,
    },
  ],

  dates: [] as string[],
  selectedDate: "",
};

const gridSlice = createSlice({
  name: "gridData",
  initialState: initialState,
  reducers: {
    updateGridData: (state, action) => {
      state.gridData = action.payload;
    },
    updateGridTradedEnergy: (state, action) => {
      state.tradedEnergy = action.payload;
      state.dates = action.payload.map(
        (data: (typeof initialState.tradedEnergy)[0]) => data.date
      );
    },
    updateGridPeakGeneration: (state, action) => {
      state.peakGeneration = action.payload;
      state.dates = [
        ...new Set([
          ...state.dates,
          ...action.payload.map((data: { date: string }) => data.date),
        ]),
      ];
    },
    updateGridPeakDemand: (state, action) => {
      state.peakDemand = action.payload;
      state.dates = [
        ...new Set([
          ...state.dates,
          ...action.payload.map((data: { date: string }) => data.date),
        ]),
      ];
    },

    updateSelectedDate: (state, action) => {
      state.selectedDate = action.payload;
    },
  },
});

export const {
  updateGridData,
  updateGridTradedEnergy,
  updateGridPeakGeneration,
  updateGridPeakDemand,
  updateSelectedDate,
} = gridSlice.actions;

export default gridSlice.reducer;
