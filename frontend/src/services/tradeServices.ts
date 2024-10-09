import apiInstance from "./apiInstance";
import {
  energyReportUri,
  resolveAllTradesUri,
  tradeBuyAllUri,
  tradeSellAllUri,
  transactionByGridUri,
} from "./endpoint";

const tradeServices = {
  async getAllSellTrade() {
    return apiInstance({ endpoint: tradeSellAllUri });
  },
  async getAllBuyTrade() {
    return apiInstance({ endpoint: tradeBuyAllUri });
  },

  async getResolveAllTrades() {
    return apiInstance({ endpoint: resolveAllTradesUri });
  },

  async getAllTransactionsByGridId(gridId: number = 1) {
    return apiInstance({ endpoint: transactionByGridUri(gridId) });
  },

  async getEnergyReport({
    date,
    gridId = 1,
    interval = 1,
  }: {
    date: any;
    gridId?: number;
    interval?: number;
  }) {
    return apiInstance({
      endpoint: energyReportUri({ date, gridId, interval }),
    });
  },
};

export default tradeServices;
