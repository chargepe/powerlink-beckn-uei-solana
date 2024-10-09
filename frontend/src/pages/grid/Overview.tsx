import { Card } from "@/components/ui/card";
import { useAppSelector } from "@/redux/hooks";
import { useEffect, useMemo, useState } from "react";
import DatesDD from "./DatesDD";
import { convertToAMPM } from "@/utils/config";
import { DataTable } from "@/components/ui/data-table";
import { ITransactionData, transactionsColumn } from "./transactionsColumn";
import tradeServices from "@/services/tradeServices";
import EnergyBarChart from "./EnergyChart";
import { Button } from "@/components/ui/button";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import infoIcon from "@/assets/infoIcon.svg";
import gridServices from "@/services/gridServices";
import { useToast } from "@/hooks/use-toast";

function Overview() {
  const { selectedDate: date }: any = useAppSelector((state) => state.grid);
  const [loading, setLoading] = useState(false);
  const { toast } = useToast();
  const [isUpdated, setIsUpdated] = useState(false);
  const { peakGeneration, peakDemand, tradedEnergy } = useAppSelector(
    (state) => state.grid
  );

  const [txData, setTxData] = useState<ITransactionData[]>([]);

  const getTransactionData = async () => {
    try {
      const newTxData = await tradeServices.getAllTransactionsByGridId(1);

      setTxData((prevData) => {
        const isEqual = JSON.stringify(prevData) === JSON.stringify(newTxData);
        return isEqual ? prevData : newTxData;
      });
    } catch (error) {
      console.error("Error fetching transaction data:", error);
    }
  };

  useEffect(() => {
    getTransactionData();
    const intervalId = setInterval(getTransactionData, 10000);
    return () => clearInterval(intervalId);
  }, []);

  const demand = useMemo(() => {
    return peakDemand.filter((data) => data.date === date);
  }, [date]);
  const supply = useMemo(() => {
    return peakGeneration.filter((data) => data.date === date);
  }, [date]);

  const totalTradedEnergy = useMemo(() => {
    return tradedEnergy.filter((data) => data.date === date);
  }, [date]);

  const energySurplus = useMemo(() => {
    if (supply[0]?.totalEnergyUnits && demand[0]?.totalEnergyUnits) {
      return supply[0]?.totalEnergyUnits - demand[0]?.totalEnergyUnits;
    }

    return 0;
  }, [demand, supply]);

  const onTestTransactionClick = async () => {
    setLoading(true);
    await gridServices.getAutomatedGridTx();
    setIsUpdated(true);
    setTimeout(() => setIsUpdated(false), 2000);
    setLoading(false);
    toast({
      title: "Success",
      description: "Test Transactions has been created successfully",
    });
  };

  return (
    <Card className="col-span-2 flex flex-col justify-between p-3 gap-5 items-start">
      <div className="flex items-center justify-between w-full">
        <div className="font-bold ">Overview</div>
        <DatesDD />
      </div>

      <div className="flex flex-wrap w-full gap-4">
        <Card className="flex flex-col p-2 w-[180px]">
          <div className="flex flex-col justify-between gap-1 items-start">
            <span className="text-red-500 font-bold text-2xl">
              {date ? `${demand[0]?.totalEnergyUnits || 0} Kw` : ""}
            </span>
            <div className="flex justify-between gap-1 text-sm">
              <span className="font-bold text-left">
                Peak Energy
                <br />
                Demand
              </span>
              <span>{date ? `(${convertToAMPM(demand[0]?.hour)})` : ""}</span>
            </div>
          </div>
        </Card>
        <Card className="flex flex-col p-2 w-[180px]">
          <div className="flex flex-col justify-between gap-1 items-start">
            <span className="text-green-500 font-bold text-2xl">
              {date ? `${supply[0]?.totalEnergyUnits || 0} Kw` : ""}
            </span>
            <div className="flex justify-between gap-1 text-sm">
              <span className="font-bold text-left">
                Peak Energy
                <br />
                Generated
              </span>
              <span>{date ? `(${convertToAMPM(supply[0]?.hour)})` : ""}</span>
            </div>
          </div>
        </Card>
        <Card className="flex flex-col p-2 w-[180px]">
          <div className="flex flex-col justify-between gap-1 items-start">
            <span className="text-cyan-500 font-bold text-2xl">
              {date ? `${totalTradedEnergy[0]?.totalEnergyTraded || 0} Kw` : ""}
            </span>
            <div className="flex justify-between gap-1 text-sm">
              <span className="font-bold text-left">
                Total Energy
                <br />
                Traded
              </span>
            </div>
          </div>
        </Card>
        <Card className="flex flex-col p-2 w-[180px]">
          <div className="flex flex-col justify-between gap-1 items-start">
            <span className=" font-bold text-2xl">
              {date ? `${energySurplus || 0} Kw` : ""}
            </span>
            <div className="flex justify-between gap-1 text-sm">
              <span className="font-bold text-left">
                Surplus <br />
                Energy
              </span>
            </div>
          </div>
        </Card>
      </div>

      <EnergyBarChart />

      <p className="font-bold flex justify-between w-full">
        <span>Transactions {`(${txData.length})`}</span>
        <div className="flex gap-2">
          <Button disabled={loading} onClick={onTestTransactionClick}>
            Test Transaction
          </Button>
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger asChild>
                <img src={infoIcon} className="w-4"></img>
              </TooltipTrigger>
              <TooltipContent className="">
                <p>Triggers automated transactions.{`(just for demo)`}</p>
              </TooltipContent>
            </Tooltip>
          </TooltipProvider>
        </div>
      </p>

      <DataTable
        columns={transactionsColumn}
        data={txData}
        isUpdated={isUpdated}
      />
    </Card>
  );
}

export default Overview;
