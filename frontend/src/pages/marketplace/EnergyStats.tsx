import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { useAppSelector } from "@/redux/hooks";
import { memo, useMemo } from "react";
import liveIcon from "@/assets/statusIcon.svg";

function EnergyStats() {
  const { meter, sellingTransactions } = useAppSelector((state) => state.user);
  const totalSales = useMemo(() => {
    return sellingTransactions.reduce((acc, curr) => {
      const amt = curr?.transactionAmount;
      acc += amt;
      return acc;
    }, 0);
  }, [sellingTransactions]);

  const energyBalance = useMemo(() => {
    return meter?.energyGenerated - meter?.energyConsumed;
  }, [meter]);

  return (
    <Card>
      <CardHeader className="box-shadow-md font-bold flex">
        <div className="flex justify-between">
          <span className="">Live Energy Stats</span>
          <img className="w-5" src={liveIcon}></img>
        </div>
      </CardHeader>
      <CardContent className="flex flex-col gap-4">
        <div className="grid grid-cols-2 gap-4">
          <Card className="p-4 flex flex-col">
            <span className="text-indigo-600 font-bold text-xl">
              {meter?.energyGenerated} Kwh
            </span>
            <span className="text-muted-foreground">Energy production</span>
          </Card>
          <Card className="p-4 flex flex-col">
            <span className="text-lime-300 font-bold text-xl">{meter?.energyConsumed} Kwh</span>
            <span className="text-muted-foreground">Energy consumption</span>
          </Card>
        </div>
        <div className="grid grid-cols-2 gap-4">
          <Card className="p-4 flex flex-col">
            <span className="text-black font-bold text-xl">{energyBalance} Kwh</span>
            <span className="text-muted-foreground">Energy Balance</span>
          </Card>
          <Card className="p-4 flex flex-col">
            <span className="text-green-500 font-bold text-xl">Rs.{totalSales}</span>
            <span className="text-muted-foreground">Total sales</span>
          </Card>
        </div>
      </CardContent>
    </Card>
  );
}

export default memo(EnergyStats);
