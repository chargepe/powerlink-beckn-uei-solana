import { ColumnDef } from "@tanstack/react-table";
import LedgerButton from "./LedgerButton";

export type SellData = {
  id: number;
  sellerEnergyConsumed: number;
  price: number;
  timestamp: string;
  status: string;
  transactionSignature: string;
};

export const columnsSell: ColumnDef<SellData>[] = [
  {
    accessorKey: "price",
    header: "Rate",
    cell: ({ row }) => {
      const rate = parseFloat(row.getValue("price"));
      const formatted = new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "INR",
      }).format(rate);

      return <div className="text-left font-medium">{formatted}</div>;
    },
  },
  {
    accessorKey: "sellerEnergyConsumed",
    header: "Energy Units",
    cell: ({ row }) => {
      const orderLimit = parseFloat(row.getValue("sellerEnergyConsumed"));
      const formatted = orderLimit + " Kwh";
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
        <div className="p-2 font-medium bg-green-500 text-white rounded-md">
          SELL
        </div>
      );
    },
  },
  {
    accessorKey: "transactionSignature",
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
