import { Card } from "@/components/ui/card";
import { memo, useState } from "react";
import SellEnergyCard from "./SellEnergyCard";
import BuyCard from "./BuyCard";

function SellBuyCard({ setUpdated }: { setUpdated: (val: boolean) => void }) {
  const [cardstate, setCardstate] = useState("sell");
  return (
    <div>
      <div className="grid grid-cols-4 px-6">
        <Card className="p-2 flex col-start-2 col-span-4 ">
          <button
            className=" flex-1 p-2 rounded-md disabled:bg-green-500 disabled:text-white font-bold  text-black bg-inherit"
            disabled={cardstate === "buy"}
            onClick={() => setCardstate("buy")}
          >
            BUY
          </button>
          <button
            className=" flex-1 p-2 rounded-md disabled:bg-green-500 disabled:text-white font-bold text-black bg-inherit"
            disabled={cardstate === "sell"}
            onClick={() => setCardstate("sell")}
          >
            SELL
          </button>
        </Card>
      </div>

      {cardstate === "sell" ? (
        <SellEnergyCard setUpdated={setUpdated} />
      ) : (
        <BuyCard setUpdated={setUpdated} />
      )}
    </div>
  );
}

export default memo(SellBuyCard);
