import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import { updateSelectedDate } from "@/redux/slices/gridSlice";
import { useEffect, useState } from "react";

function DatesDD() {
  const { dates } = useAppSelector((state) => state.grid);
  const [value, setValue] = useState<string>(dates[0]);
  const dispatch = useAppDispatch();

  useEffect(() => {
    if (dates.length > 0 && value === undefined) {
      setValue(dates[0]);
      dispatch(updateSelectedDate(dates[0]));
    }
  }, [dates]);

  const onValueChange = (value: string) => {
    setValue(value);
    dispatch(updateSelectedDate(value));
  };
  return (
    <div className="flex flex-col items-start text-gray-400 m-3">
      <p>Select a date to see analytics</p>
      <Select
        value={value}
        onValueChange={onValueChange}
        defaultValue={dates[0]}
      >
        <SelectTrigger className="w-[180px]">
          <SelectValue placeholder={dates[0]} />
        </SelectTrigger>
        <SelectContent defaultValue={dates[0]}>
          {Array.isArray(dates) &&
            dates.map((date) => {
              return (
                <SelectItem key={date} value={date}>
                  {date}
                </SelectItem>
              );
            })}
        </SelectContent>
      </Select>
    </div>
  );
}

export default DatesDD;
