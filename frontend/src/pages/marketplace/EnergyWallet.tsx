import { Card, CardContent, CardTitle } from "@/components/ui/card";
import { useAppSelector } from "@/redux/hooks";
import { formattedINR } from "@/utils/config";
import subtract from "@/assets/Subtract.svg";
import { memo } from "react";

function EnergyWallet() {
  const { wallet } = useAppSelector((state) => state.user);
  return (
    <Card
    className="p-5 bg-gradient-to-r from-lime-500 to-green-500"
      // className="p-5 bg-auto bg-no-repeat bg-left"
      // style={{ backgroundImage: `url(${subtract})` }}
    >
      <CardTitle className="font-bold text-xl text-left p-6 pl-0 text-white ">
        My Wallet
      </CardTitle>

      <Card className="py-2 z-10 bg-white bg-opacity-90 top-1/4">
        <CardContent className="flex flex-col items-start">
          <span className="text-muted-foreground text-lg">Balance</span>
          <span className="font-bold text-2xl">{formattedINR(wallet?.balance)}</span>
        </CardContent>
      </Card>

      {/* Wallet content */}
    </Card>
  );
}

export default memo(EnergyWallet);
