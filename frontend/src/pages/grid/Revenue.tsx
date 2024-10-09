import { useMemo } from "react";
import { useAppSelector } from "@/redux/hooks";
import { Card } from "@/components/ui/card";
import { formattedINR } from "@/utils/config";
import leafIcon from "@/assets/leafIcon.svg";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";

import infoIcon from "@/assets/infoIcon.svg";

function Revenue() {
  const {
    gridData: {
      buyPricePerUnit,
      sellPricePerUnit,
      buyPricePerUnitNR,
      reference,
      capacity,
      remainingCapacity,
    },
  } = useAppSelector((state) => state.grid);

  const energyTransacted: number =
    capacity && remainingCapacity ? capacity - remainingCapacity : 0;

  const expenditure: number =
    energyTransacted && buyPricePerUnit
      ? energyTransacted * buyPricePerUnit
      : 0;
  const revenue: number =
    energyTransacted && sellPricePerUnit
      ? energyTransacted * sellPricePerUnit
      : 0;

  const profitMargin: number = useMemo(() => {
    return (buyPricePerUnitNR - buyPricePerUnit) * energyTransacted;
  }, [energyTransacted]);

  function ProfitMarginTooltip() {
    return (
      <div className="flex flex-col items-start gap-1">
        <p>Grid buys renewable energy at: Rs.{buyPricePerUnit}/unit</p>
        <p>Grid buys non-renewable energy at: Rs.{buyPricePerUnitNR}/unit</p>
        <p>Grid sells energy at Rs.{sellPricePerUnit}/unit</p>
        <p>
          Profit margin for buy of renewables Rs.
          {buyPricePerUnitNR - buyPricePerUnit}/unit
        </p>
        <a target="_blank" href={reference} className="text-blue-400">
          Click for more info
        </a>
      </div>
    );
  }

  function ExpenditureTooltip() {
    return (
      <div className="flex flex-col items-start gap-1">
        <p>This is the total amount spent on purchasing energy.</p>
        <p>
          Calculated as: {energyTransacted} units * Rs.{buyPricePerUnit}/unit
        </p>
      </div>
    );
  }

  function RevenueTooltip() {
    return (
      <div className="flex flex-col items-start gap-1">
        <p>This is the total amount earned from selling energy.</p>
        <p>
          Calculated as: {energyTransacted} units * Rs.{sellPricePerUnit}/unit
        </p>
      </div>
    );
  }

  return (
    <Card className="flex flex-col justify-between items-start mb-4 p-6">
      <div className="flex items-center gap-5 mb-4">
        <img src={leafIcon} className="w-12 h-12" />
        <div className="flex flex-col items-start">
          <span className="font-bold text-lg flex items-center gap-2">
            <span>You've saved</span>
            <span>
              <TooltipProvider>
                <Tooltip>
                  <TooltipTrigger asChild>
                    <img src={infoIcon} className="cursor-pointer" />
                  </TooltipTrigger>
                  <TooltipContent>{ProfitMarginTooltip()}</TooltipContent>
                </Tooltip>
              </TooltipProvider>
            </span>
          </span>
          <span className="font-bold text-3xl">
            {formattedINR(profitMargin)}
          </span>
        </div>
      </div>

      <div className="flex flex-col items-start mb-4">
        <span className="font-bold text-lg text-gray-500">
          Total Expenditure
        </span>
        <span className="font-bold text-lg">
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger asChild>
                <span className="cursor-pointer">
                  {formattedINR(expenditure)}
                </span>
              </TooltipTrigger>
              <TooltipContent>{ExpenditureTooltip()}</TooltipContent>
            </Tooltip>
          </TooltipProvider>
        </span>
      </div>
      <div className="flex flex-col items-start">
        <span className="font-bold text-lg text-gray-500">Total Revenue</span>
        <span className="font-bold text-lg">
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger asChild>
                <span className="cursor-pointer">{formattedINR(revenue)}</span>
              </TooltipTrigger>
              <TooltipContent>{RevenueTooltip()}</TooltipContent>
            </Tooltip>
          </TooltipProvider>
        </span>
      </div>
    </Card>
  );
}

export default Revenue;
