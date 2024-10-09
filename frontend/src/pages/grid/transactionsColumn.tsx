import { dateOnly, time } from "@/utils/config";
import { ColumnDef } from "@tanstack/react-table";
import LedgerButton from "../marketplace/energyForBid/LedgerButton";

export type ITransactionData = {
  transactionAmount: number;
  transactionSignature: string;
  transactionStatus: string;
  price: number;
  date: string;
};

export const transactionsColumn: ColumnDef<ITransactionData>[] = [
  {
    accessorKey: "date",
    header: "Date & Time",
    cell: ({ row }) => {
      const date: string = row.getValue("date");
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
    accessorKey: "transactionAmount",
    header: "Energy Units",
    cell: ({ row }) => {
      const orderLimit = parseFloat(row.getValue("transactionAmount"));
      const formatted = orderLimit + "Kwh";
      return <div className="text-left font-medium">{formatted}</div>;
    },
  },
  {
    accessorKey: "price",
    header: "Rate",
    cell: ({ row }) => {
      // if (row.getValue("orderType") === "BUY") {
      //   return <div className="text-left font-medium">NA</div>;
      // }

      const rate = parseFloat(row.getValue("price"));
      const formatted = new Intl.NumberFormat("en-US", {
        style: "currency",
        currency: "INR",
      }).format(rate);

      return <div className="text-left font-medium">{formatted}</div>;
    },
  },
  {
    accessorKey: "transactionSignature",
    header: "Transaction Signature",
    cell: ({ row }) => {
      const signature: string = row.getValue("transactionSignature");
      return (
        <div className="font-medium text-left w-32 truncate">
          {signature || "NA"}
        </div>
      );
    },
  },

  {
    header: "Ledger Transaction",
    cell: ({ row }) => {
      return (
        <LedgerButton
          status="COMPLETE"
          signature={row.getValue("transactionSignature")}
        />
      );
    },
  },
];
