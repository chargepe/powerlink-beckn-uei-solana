import apiInstance from "./apiInstance";
import {
  automatedTxOnGridUri,
  getGridsuri,
  gridPeakDemandUri,
  gridPeakGenerationUri,
  gridTradedEnergyUri,
} from "./endpoint";

const gridServices = {
  async getGrid(id = 1) {
    return apiInstance({ endpoint: getGridsuri(id) });
  },
  //analytics-controller
  async getGridTradedEnergy(id: number = 1) {
    return apiInstance({ endpoint: gridTradedEnergyUri(id) });
  },
  async getGridPeakGeneration(id: number = 1) {
    return apiInstance({ endpoint: gridPeakGenerationUri(id) });
  },
  async getGridPeakDemand(id: number = 1) {
    return apiInstance({ endpoint: gridPeakDemandUri(id) });
  },

  async getAutomatedGridTx(gridid: number = 1) {
    return apiInstance({ endpoint: automatedTxOnGridUri(gridid) });
  }
};

export default gridServices;
