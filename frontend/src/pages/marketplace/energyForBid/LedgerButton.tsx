import { Dialog, DialogContent, DialogHeader } from "@/components/ui/dialog";
import services from "@/services/services";
import {
  DialogDescription,
  DialogTitle,
  DialogTrigger,
} from "@radix-ui/react-dialog";
import { useState } from "react";
import { CodeBlock } from "react-code-blocks";

function LedgerButton({ status, signature }: { status: any; signature: any }) {
  const [solanaData, setSolanaData] = useState();

  const handleClick = async () => {
    const data = await services.getSolanaTransaction(signature);
    console.log(data);
    setSolanaData(data);
  };

  return (
    <div className="flex">
      <Dialog onOpenChange={handleClick}>
        <DialogTrigger asChild>
          <button
            className="self-left font-bold p-2 bg-blue-500 text-white radius-md disabled:bg-[#3B82F666] rounded-md"
            disabled={status !== "COMPLETE" || !signature}
          >
            VALIDATE
          </button>
        </DialogTrigger>
        <DialogContent className="max-w-screen  m-4 mr-4">
          <DialogHeader>
            <DialogTitle>Buy Transaction</DialogTitle>
            <DialogDescription>
              You can view the transaction completed on Solana for this buy
              request.
            </DialogDescription>
          </DialogHeader>

          <div className="min-w-fit max-h-96 overflow-auto">
            {" "}
            {/* Make scrollable */}
            {solanaData ? (
              <CodeBlock
                text={JSON.stringify(solanaData, null, 2)} // Pretty print JSON
                language="javascript"
                wrapLongLines
                showLineNumbers={true}
                theme="atom-one-dark"
              />
            ) : (
              <p>Loading transaction data...</p>
            )}
          </div>
        </DialogContent>
      </Dialog>
    </div>
  );
}

export default LedgerButton;
