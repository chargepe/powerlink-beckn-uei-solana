export const baseUrl = import.meta.env.VITE_BASE_URL;
const uri = {
  registerUser: "/users/register",
  getUserUri: (userId: number) => `/users/${userId}`,
  selluri: "/trade/sell",
  buyuri: "/trade/buy",
  getGridsuri: (gridId: number = 1) => `/grids/${gridId}`,
  gridTradedEnergyUri: (gridId: number = 1) =>
    `/analytics/tradedEnergy/${gridId}`,
  gridPeakGenerationUri: (gridId: number = 1) =>
    `/analytics/peakGeneration/${gridId}`,
  gridPeakDemandUri: (gridId: number = 1) => `/analytics/peakDemand/${gridId}`,
  tradeSellAllUri: "/trade/sell/all",
  tradeBuyAllUri: "/trade/buy/all",
  resolveAllTradesUri: "/trade/endOfDay",
  checkIfUserExists: (email: string) => `/users/checkIfUserExists/${email}`,
  solanaTransactionUri: (transactionSignature: string) =>
    `/users/transaction/${transactionSignature}`,
  transactionByGridUri: (gridId: number = 1) =>
    `/trade/transaction/grid/${gridId}`,
  energyReportUri: ({
    date,
    gridId = 1,
    interval = 1,
  }: {
    date: any;
    gridId: number;
    interval: number;
  }) =>
    `/analytics/energyReport?gridId=${gridId}&date=${date}&interval=${interval}`,

  automatedTxOnGridUri: (gridId:number) => `/grids/automated/${gridId}`
};

export const {
  registerUser,
  getUserUri,
  selluri,
  buyuri,
  getGridsuri,
  gridTradedEnergyUri,
  gridPeakGenerationUri,
  gridPeakDemandUri,
  tradeSellAllUri,
  tradeBuyAllUri,
  resolveAllTradesUri,
  checkIfUserExists,
  solanaTransactionUri,
  transactionByGridUri,
  energyReportUri,
  automatedTxOnGridUri
} = uri;
