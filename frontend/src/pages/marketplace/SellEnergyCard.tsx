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
import { useMemo } from "react";

function SellEnergyCard({
  setUpdated,
}: {
  setUpdated: (val: boolean) => void;
}) {
  const { id, meter } = useAppSelector((state) => state.user);
  const energyBalance = useMemo(() => {
    return meter?.energyGenerated - meter?.energyConsumed;
  }, [meter]);

  const formSchema = z.object({
    energyUnits: z
      .number()
      .min(1, { message: "You must sell at least 1 watt." }),
    // .max(energyBalance, {
    //   message: energyBalance
    //     ? `You can sell a maximum of ${energyBalance} watts.`
    //     : "You don't have enough energy balance",
    // }),
    pricePerUnit: z.number(),
  });

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      energyUnits: undefined,
    },
  });

  async function onSubmit(values: z.infer<typeof formSchema>) {
    if (id) {
      await services.sellRequest({
        sellerId: id,
        ...values,
      });

      setUpdated(true);
      setTimeout(() => {
        setUpdated(false);
      }, 1000);
    }
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
                      <span>
                        max: {energyBalance ? energyBalance + " Kwh" : "NB"}{" "}
                      </span>
                    </FormDescription>
                    <FormMessage className="text-left" />
                  </div>
                </div>
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="pricePerUnit"
            render={({ field }) => (
              <FormItem>
                <div className="flex items-center gap-5">
                  <FormLabel className="whitespace-nowrap">
                    Rate in Rs/Kwh:
                  </FormLabel>
                  <FormControl>
                    <Input
                      type="number"
                      placeholder="Enter rate for energy"
                      {...field}
                      onChange={(e) =>
                        field.onChange(parseFloat(e.target.value))
                      }
                    />
                  </FormControl>
                </div>
              </FormItem>
            )}
          />
          <div className="grid grid-cols-6">
            <Button className="col-start-2 px-12 bg-green-500 " type="submit">
              Sell
            </Button>
          </div>
        </form>
      </Form>
    </div>
  );
}

export default SellEnergyCard;
