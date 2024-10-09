import apiInstance from "./apiInstance";
import {
  buyuri,
  checkIfUserExists,
  getUserUri,
  registerUser,
  selluri,
  solanaTransactionUri,
} from "./endpoint";

interface IBuyReq {
  buyerId: number | null;
  energyUnits: number;
  maxPricePerUnit: number;
}

interface ISellReq {
  sellerId: number;
  energyUnits: number;
  pricePerUnit: number;
}

interface IRegisterReq {
  name?: string;
  address?: string;
  email: string;
  energyExchangeCapability?: string;
  industryType?: string;
  initialBalance?: number | null;
  gridId: number;
}

const services = {
  async registerUser(body: IRegisterReq) {
    return apiInstance({ endpoint: registerUser, method: "POST", body: body });
  },
  async getUserData(id: number) {
    return apiInstance({ endpoint: getUserUri(id) });
  },
  async buyRequest(body: IBuyReq) {
    return apiInstance({ endpoint: buyuri, method: "POST", body: body });
  },
  async sellRequest(body: ISellReq) {
    return apiInstance({ endpoint: selluri, method: "POST", body: body });
  },
  async getUerExist(email: string) {
    return apiInstance({ endpoint: checkIfUserExists(email) });
  },
  async getSolanaTransaction(transactionSignature: string) {
    return apiInstance({
      endpoint: solanaTransactionUri(transactionSignature),
    });
  },
};

export default services;
