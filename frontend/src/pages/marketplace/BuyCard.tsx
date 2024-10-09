import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "@/components/ui/button";
import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { useAppSelector } from "@/redux/hooks";
import services from "@/services/services";

export default function BuyCard({
  setUpdated,
}: {
  setUpdated: (val: boolean) => void;
}) {
  const {
    id,
    wallet: { balance },
  } = useAppSelector((state) => state.user);

  const formSchema = z
    .object({
      energyUnits: z
        .number()
        .min(1, { message: "You must buy at least 1 watt." })
        .max(balance, {
          message: `You can buy a maximum of ${balance} watts.`,
        }),
      maxPricePerUnit: z.number(),
    })
    .refine((data) => data.energyUnits * data.maxPricePerUnit <= 1000, {
      message: `The total price cannot exceed ₹1000. Adjust the energy units.`,
      path: ["energyUnits"], // This points the error to the energyUnits field.
    });

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      energyUnits: undefined,
    },
  });

  async function onSubmit(values: z.infer<typeof formSchema>) {
    const body = {
      buyerId: id,
      sellRequestId: 1,
      energyUnits: values?.energyUnits,
      maxPricePerUnit: values?.maxPricePerUnit,
    };
    await services.buyRequest(body);
    setUpdated(true);
    setTimeout(() => {
      setUpdated(false);
    }, 1000);
  }

  return (
    <div className="mt-11 px-6">
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
          <FormField
            control={form.control}
            name="energyUnits"
            render={({ field }) => (
              <FormItem>
                <div className="flex items-center gap-5">
                  <FormLabel className="whitespace-nowrap">
                    Energy in Kwh:
                  </FormLabel>
                  <div className="w-full">
                    <FormControl>
                      <Input
                        type="number"
                        placeholder="Enter watts amount"
                        {...field}
                        onChange={(e) =>
                          field.onChange(parseFloat(e.target.value))
                        }
                      />
                    </FormControl>
                    <FormDescription className="flex justify-between">
                      <span>min: 1 Kwh</span>
                    </FormDescription>
                    <FormMessage className="text-left" />
                  </div>
                </div>
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="maxPricePerUnit"
            render={({ field }) => (
              <FormItem>
                <div className="flex items-center gap-8">
                  <FormLabel className="whitespace-nowrap">
                    Max Rs/Kwh:
                  </FormLabel>
                  <FormControl>
                    <Input
                      type="number"
                      placeholder="Enter watts amount"
                      {...field}
                      onChange={(e) =>
                        field.onChange(parseFloat(e.target.value))
                      }
                    />
                  </FormControl>
                  <FormMessage />
                </div>
              </FormItem>
            )}
          />
          <div className="grid grid-cols-6">
            <Button className="col-start-2 px-12 bg-green-500 " type="submit">
              Buy
            </Button>
          </div>
        </form>
      </Form>
    </div>
  );
}
