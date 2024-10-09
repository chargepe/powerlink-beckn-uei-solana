import { ColumnDef } from "@tanstack/react-table";
import LedgerButton from "./LedgerButton";
import { Badge } from "@/components/ui/badge"
import { dateOnly, time } from "@/utils/config";

export type IData = {
  id: number;
  energyUnits: number;
  price: number;
  timestamp: string;
  status: string;
  orderType: string;
  transactionSignature: string;
};

export const tradesColumn: ColumnDef<IData>[] = [
  {
    accessorKey: "orderType",
    header: "Order Type",
    cell: ({ row }) => {

      const orderType:"BUY"|"SELL" = row.getValue("orderType");
      return (
        <Badge className={orderType==='BUY'?`bg-[#D9E8DA] text-[#4A7659]`:`text-[#8C730F] bg-[#E8DED9]`} variant="outline" >
          {orderType}
        </Badge>
      );
    },
  },
  {
    accessorKey: "timestamp",
    header: "Date & Time",
    cell: ({ row }) => {
      const date: string = row.getValue("timestamp");
      const formattedTime = time(date);
      const formattedDate = dateOnly(date);
      return (
        <div className="text-left font-medium">
          {formattedDate}
          <br />
          <span>{formattedTime}</span>
        </div>
      );
    },
  },
  {
    accessorKey: "energyUnits",
    header: "Energy Units",
    cell: ({ row }) => {
      const orderLimit = parseFloat(row.getValue("energyUnits"));
      const formatted = orderLimit + " Kwh";
      return <div className="text-left font-medium">{formatted}</div>;
    },
  },
  {
    accessorKey: "price",
    header: "Rate",
    cell: ({ row }) => {

      if (row.getValue("orderType") === "BUY") {
        return <div className="text-left font-medium">NA</div>;
      }

      const rate = parseFloat(row.getValue("price"));
      const formatted = new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "INR",
      }).format(rate);

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
    accessorKey: "transactionSignature",
    header: "Transaction Signature",
    cell: ({ row }) => {
      const signature: string = row.getValue("transactionSignature");
      return <div className="font-medium text-center w-16 truncate">{signature || "NA"}</div>;
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
