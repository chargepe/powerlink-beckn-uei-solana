import { ColumnDef } from "@tanstack/react-table";
import LedgerButton from "./LedgerButton";

export type BuyData = {
  id: number;
  buyerEnergyConsumed: number;
  timestamp: string;
  status: string;
  transactionSignature: string;
};

export const columnsBuy: ColumnDef<BuyData>[] = [
  {
    accessorKey: "buyerEnergyConsumed",
    header: "Energy Units",
    cell: ({ row }) => {
      const value = row.getValue("buyerEnergyConsumed");
      const formatted = value + " Kwh";
      return <div className="text-left font-medium">{formatted}</div>;
    },
  },
  {
    accessorKey: "timestamp",
    header: "Time",
    cell: ({ row }) => {
      const time: string = row.getValue("timestamp");
      const formatted = time;
      return <div className="text-left font-medium">{formatted}</div>;
    },
  },
  {
    accessorKey: "status",
    header: "Status",
    cell: ({ row }) => {
      const status: string = row.getValue("status");
      return <div className="text-left font-medium">{status}</div>;
    },
  },
  {
    header: "Order Type",
    cell: () => {
      return (
        <div className="font-medium bg-green-500 text-white rounded-md p-2">
          BUY
        </div>
      );
    },
  },
  {
    accessorKey:"transactionSignature",
    header: "transactionSignature",
    cell: ({ row }) => {
      const signature: string = row.getValue("transactionSignature");
      return <div className="font-medium text-center">{signature || "NA"}</div>;
    },
  },
  {
    header: "Ledger Transaction",
    cell: ({ row }) => {
      return (
        <LedgerButton
          status={row.getValue("status")}
          signature={row.getValue("transactionSignature")}
        />
      );
    },
  },
];
