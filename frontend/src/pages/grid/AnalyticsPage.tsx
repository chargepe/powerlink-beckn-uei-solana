import React, { useState, useEffect } from "react";
import Overview from "./Overview";
import Revenue from "./Revenue";
import GridDetails from "./GridDetails";
import gridServices from "@/services/gridServices";
import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import {
  updateGridData,
  updateGridPeakDemand,
  updateGridPeakGeneration,
  updateGridTradedEnergy,
} from "@/redux/slices/gridSlice";

import { Button } from "@/components/ui/button";
import tradeServices from "@/services/tradeServices";

const Modal = ({ showModal, closeModal }) => {
  const [showMore, setShowMore] = useState(false);

  const toggleShowMore = () => {
    setShowMore(!showMore);
  };

  if (!showModal) return null;

  return (
    <div className="fixed inset-0 bg-gray-500 bg-opacity-75 flex items-center justify-center z-50 ">
      <div className="bg-white p-6 rounded-lg shadow-lg max-w-lg w-full max-h-screen overflow-y-auto m-5">
        <h2 className="text-2xl font-bold mb-4">
          Welcome to the P2P Energy Dashboard!
        </h2>
        <p className="mb-6 text-left">
          1. This is the admin dashboard of the grid for Charlie, the Assistant
          Chief of the Distribution company that manages the grid for the city.
          <br />
          <br />
          2. Currently, we have made this page publicly available to demo our
          application. In the future, we will secure it with credentials.
          <br />
          <br />
          3. Solana blockchain has been used to manage the transactions.
          <br />
          <br />
          4. Since we used our own ledger, the transactions will be unique to
          our website only.
        </p>

        {/* Show More Section */}

        {showMore && (
          <div className="mt-4">
            <h3 className="text-xl font-semibold">Additional Details</h3>
            <div className="mt-2 text-left">
              <ul className="list-disc list-inside space-y-2">
                <li>
                  Since we had limited free SOL for our utilization, we have
                  assumed that in our application,{" "}
                  <b>1 Rupee = 0.00203928 SOL</b>.
                </li>
                <li>
                  For ease of viewing real-time transactions, we have provided
                  an <b>Automate Transactions</b> button, which will perform{" "}
                  <b>15 automatic transactions</b> to showcase the real-time
                  application.
                </li>
                <li>
                  There is also a <b>Settle Transactions</b> button at the
                  bottom of the page. It will settle all unsettled transactions
                  by buying the unsold renewable energy at a fixed rate and
                  selling it at a fixed rate.
                </li>
                <li>
                  We assume that the non-renewable supply from power plants is
                  sufficient for the daily requirements of the users. For any
                  extra energy, users are engaging in <b>P2P trading</b>.
                </li>
                <li>
                  The grid has a maximum capacity of <b>100,000 kWh</b> of
                  renewable energy storage.
                </li>
                <li>
                  We assume that Charlie is responsible for decisions related to{" "}
                  <b>grid capacity, storage planning,</b> and{" "}
                  <b>financial forecasting</b> based on P2P energy trading data.
                </li>
              </ul>
            </div>
          </div>
        )}

        <button className="text-blue-500 mt-2" onClick={toggleShowMore}>
          {showMore ? "Show Less" : "Show More ( **FOR REVIEWERS** )"}
        </button>

        <div className="mt-6">
          <Button className="bg-blue-500" onClick={closeModal}>
            Got it!
          </Button>
        </div>
      </div>
    </div>
  );
};

function AnalyticsPage() {
  const dispatch = useAppDispatch();
  const [showModal, setShowModal] = useState(true);

  const { gridData, tradedEnergy, peakDemand, peakGeneration } = useAppSelector(
    (state) => state.grid
  );

  const closeModal = () => setShowModal(false);

  // Poll all APIs in parallel
  const pollAllGridData = async () => {
    try {
      const [newGridData, newTradedEnergy, newPeakGeneration, newPeakDemand] =
        await Promise.all([
          gridServices.getGrid(1),
          gridServices.getGridTradedEnergy(1),
          gridServices.getGridPeakGeneration(1),
          gridServices.getGridPeakDemand(1),
        ]);

      JSON.stringify(gridData) !== JSON.stringify(newGridData) &&
        dispatch(updateGridData(newGridData));
      JSON.stringify(tradedEnergy) !== JSON.stringify(newTradedEnergy) &&
        dispatch(updateGridTradedEnergy(newTradedEnergy));
      JSON.stringify(peakGeneration) !== JSON.stringify(newPeakGeneration) &&
        dispatch(updateGridPeakGeneration(newPeakGeneration));
      JSON.stringify(peakDemand) !== JSON.stringify(newPeakDemand) &&
        dispatch(updateGridPeakDemand(newPeakDemand));
    } catch (error) {
      console.log("Error fetching data:", error);
    }
  };

  useEffect(() => {
    pollAllGridData();
    const intervalId = setInterval(pollAllGridData, 10000);

    return () => clearInterval(intervalId);
  }, []);

  const resolveTrades = async () => {
    try {
      await tradeServices.getResolveAllTrades();
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div>
      {/* Modal */}
      <Modal showModal={showModal} closeModal={closeModal} />

      <h2 className="font-bold text-2xl text-left mb-5">
        P2P Energy Trading Dashboard
      </h2>

      <div className="grid grid-cols-4 items-start gap-4">
        <div className="col-span-3">
          <Overview />
        </div>
        <div className="flex flex-col gap-4">
          <Revenue />
          <GridDetails />
        </div>
      </div>

      <div className="my-12 border-b text-center ">
        <div className="leading-none px-2 inline-block text-sm text-gray-400 tracking-wide font-medium bg-white transform translate-y-1/2">
          Resolves all Trades on day end
        </div>
      </div>
      <Button className="bg-green-700" onClick={resolveTrades}>
        Resolve Open Trades
      </Button>
    </div>
  );
}

export default AnalyticsPage;
