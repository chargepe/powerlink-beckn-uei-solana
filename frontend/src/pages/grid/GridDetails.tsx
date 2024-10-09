import { Card } from "@/components/ui/card";
import { useAppSelector } from "@/redux/hooks";
import { formattedINR } from "@/utils/config";
import liveStatusIcon from "@/assets/liveStatusIcon.svg";
import line10 from "@/assets/Line 10.svg";
import {
  Tooltip,
  TooltipTrigger,
  TooltipContent,
  TooltipProvider,
} from "@/components/ui/tooltip";

function GridDetails() {
  const {
    gridData: {
      capacity,
      remainingCapacity,
      buyPricePerUnit,
      sellPricePerUnit,
      reference,
      location,
      status,
    },
  } = useAppSelector((state) => state.grid);

  return (
    <TooltipProvider>
      <Card className="flex flex-col items-start p-6 w-full gap-6">
        <div className="font-bold text-2xl">Grid Details</div>
        <div className="flex justify-between w-full items-center gap-4">
          <Tooltip>
            <TooltipTrigger asChild>
              <Card className="flex flex-col bg-gray-100 grow p-2 items-start">
                <span className="font-bold text-sm text-green-300 cursor-pointer">
                  Selling Price
                </span>
                <span className="font-bold text-2xl">
                  {formattedINR(sellPricePerUnit)}
                </span>
              </Card>
            </TooltipTrigger>
            <TooltipContent
              side="top"
              className="bg-white text-blue-500 underline p-2 rounded font-bold"
            >
              <a target="_blank" rel="noopener noreferrer" href={reference}>
                Reference Link
              </a>
            </TooltipContent>
          </Tooltip>

          <Tooltip>
            <TooltipTrigger asChild>
              <Card className="flex flex-col bg-gray-100 grow p-2 items-start">
                <span className="font-bold text-sm text-orange-200 cursor-pointer">
                  Buying Price
                </span>
                <span className="font-bold text-2xl">
                  {formattedINR(buyPricePerUnit)}
                </span>
              </Card>
            </TooltipTrigger>
            <TooltipContent
              side="top"
              className="bg-white text-blue-500 underline p-2 rounded font-bold"
            >
              <a target="_blank" rel="noopener noreferrer" href={reference}>
                Reference Link
              </a>
            </TooltipContent>
          </Tooltip>
        </div>

        <div className="flex justify-between w-full gap-6 items-center">
          <div className="flex flex-col items-start">
            <span className="font-bold text-sm text-gray-500">Location</span>
            <span className="font-bold text-2xl">{location}</span>
          </div>
          <div className="flex flex-col grow items-start">
            <span className="font-bold text-sm text-gray-500">Status </span>
            <span className="font-bold text-2xl flex gap items-center">
              <img src={liveStatusIcon} alt="Live Status" />
              {status}
            </span>
          </div>
        </div>

        <div className="w-full flex flex-col gap-4 items-start">
          <div className="font-bold text-2xl">Storage Capacity</div>
          <img src={line10} alt="Line" />
          <div className="flex justify-between w-full">
            <div className="flex flex-col items-start">
              <span className="font-bold text-sm text-gray-500">
                Total Capacity
              </span>
              <span className="font-bold text-sm ">{capacity} Kw</span>
            </div>
            <div className="flex flex-col">
              <span className="font-bold text-sm text-gray-500">Remaining</span>
              <span className="font-bold text-sm text-lime-400">
                {remainingCapacity} Kw
              </span>
            </div>
          </div>
        </div>
      </Card>
    </TooltipProvider>
  );
}

export default GridDetails;
