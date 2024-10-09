import { DataTable } from "@/components/ui/data-table";
import tradeServices from "@/services/tradeServices";
import { memo, useEffect, useState } from "react";
import { useAppSelector } from "@/redux/hooks";
import { IData, tradesColumn } from "./tradesColumn";

const DemoPage = ({ isUpdated }: { isUpdated: boolean }) => {
  const [tradeData, setTradeData] = useState<IData[]>([]);

  const { id } = useAppSelector((state) => state.user);

  const getAllTradeData = async () => {
    try {
      const [sellData, buyData] = await Promise.all([
        tradeServices.getAllSellTrade(),
        tradeServices.getAllBuyTrade(),
      ]);

      const formattedBuyData = buyData.reduce((acc: any[], item: any) => {
        if (item.buyerId === id) {
          acc.push({
            id: item.buyerId,
            energyUnits: item.buyerEnergyConsumed,
            price: "NA",
            timestamp: item.timestamp,
            status: item.status,
            orderType: "BUY",
            transactionSignature: item.transactionSignature,
          });
        }
        return acc;
      }, []);

      const formattedSellData = sellData.reduce((acc: any[], item: any) => {
        if (item.sellerId === id) {
          acc.push({
            id: item.sellerId,
            energyUnits: item.sellerEnergyConsumed,
            price: item.price,
            timestamp: item.timestamp,
            status: item.status,
            orderType: "SELL",
            transactionSignature: item.transactionSignature,
          });
        }
        return acc;
      }, []);

      const combinedData = [...formattedBuyData, ...formattedSellData];

      setTradeData((prevData) => {
        const isEqual =
          JSON.stringify(prevData) === JSON.stringify(combinedData);
        return isEqual ? prevData : combinedData;
      });
      // setTradeData(combinedData);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    getAllTradeData();
    const intervalId = setInterval(getAllTradeData, 15000);

    return () => clearInterval(intervalId);
  }, []);

  useEffect(() => {
    getAllTradeData();
  }, [isUpdated]);

  return (
    <div className="container mx-auto py-10 ">
      <h3 className="mb-4 font-bold text-left text-2xl ">Trading Status</h3>
      <div className="flex gap-4">
        <DataTable
          isUpdated={isUpdated}
          columns={tradesColumn}
          data={tradeData}
        />
      </div>
    </div>
  );
};

export default memo(DemoPage);
