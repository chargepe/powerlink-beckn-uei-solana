import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import EnergyWallet from "./EnergyWallet";
import EnergyForBidTable from "./energyForBid";
import EnergyStats from "./EnergyStats";
import { useEffect, useState } from "react";
import services from "@/services/services";
import { updateUser } from "@/redux/slices/userSlice";
import SellBuyCard from "./SellBuyCard";
import { Card } from "@/components/ui/card";

const Marketplace = () => {
  const user = useAppSelector((state) => state.user);
  const id = user?.id;
  const dispatch = useAppDispatch();
  const [istableUpdated, setIstableUpdated] = useState(false);

  const getUpdatedUserData = async () => {
    try {
      if (id) {
        const data = await services.getUserData(id);
        if (JSON.stringify(user) !== JSON.stringify(data)) {
          dispatch(updateUser(data));
        }
      }
    } catch (error) {
      console.log(error);
    }
  };
  useEffect(() => {
    getUpdatedUserData();
    const intervalId = setInterval(getUpdatedUserData, 30000);

    return () => clearInterval(intervalId);
  }, []);

  useEffect(() => {
    getUpdatedUserData();
  }, [istableUpdated]);

  return (
    <div className="grid grid-cols-3 gap-4">
      <div className="col-span-1 flex flex-col gap-3">
        <EnergyWallet />
        <EnergyStats />
      </div>
      <Card className="col-span-2 p-3">
        <SellBuyCard setUpdated={(val: boolean) => setIstableUpdated(val)} />
        <EnergyForBidTable isUpdated={istableUpdated} />
      </Card>

      <div className="container flex flex-col gap-4  py-10"></div>
    </div>
  );
};

export default Marketplace;
