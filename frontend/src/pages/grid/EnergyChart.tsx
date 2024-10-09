import { useAppSelector } from "@/redux/hooks";
import tradeServices from "@/services/tradeServices";
import { useEffect, useState } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";

const EnergyBarChart = () => {
  const { selectedDate }: any = useAppSelector((state) => state.grid);
  const [chartData, setChartData] = useState([]);

  const fetchEnergyReport = async () => {
    try {
      if (selectedDate) {
        const data = await tradeServices.getEnergyReport({
          date: selectedDate,
        });

        // Transform the input data
        const transformedData = data.map((item: any) => ({
          hour: `${item.hour}-${item.hour + 1}`, // Convert hour to range format
          energyTraded: item.energyTraded,
          energyDemand: item.energyDemand,
          energyProduced: item.energyProduced,
        }));

        setChartData((prevData) => {
          return JSON.stringify(prevData) !== JSON.stringify(transformedData)
            ? transformedData
            : prevData;
        });
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    fetchEnergyReport();
    const intervalId = setInterval(fetchEnergyReport, 10000);

    return () => clearInterval(intervalId);
  }, [selectedDate]); // Trigger useEffect when selectedDate changes

  return (
    <div className="w-full overflow-x-auto">
      <div className="min-w-[800px]">
        <ResponsiveContainer width="100%" height={400}>
          <BarChart
            data={chartData}
            margin={{
              top: 20,
              right: 30,
              bottom: 5,
            }}
          >
            <defs>
              <linearGradient
                id="energyTradedGradient"
                x1="0%"
                y1="0%"
                x2="0%"
                y2="100%"
              >
                <stop offset="0%" stopColor="#0D92B7" stopOpacity={1} />
                <stop offset="100%" stopColor="#0D92B7" stopOpacity={0.3} />
              </linearGradient>
              <linearGradient
                id="energyDemandGradient"
                x1="0%"
                y1="0%"
                x2="0%"
                y2="100%"
              >
                <stop offset="0%" stopColor="#E55D3A" stopOpacity={1} />
                <stop offset="100%" stopColor="#E55D3A" stopOpacity={0.3} />
              </linearGradient>
              <linearGradient
                id="energyProducedGradient"
                x1="0%"
                y1="0%"
                x2="0%"
                y2="100%"
              >
                <stop offset="0%" stopColor="#00B33E" stopOpacity={1} />
                <stop offset="100%" stopColor="#00B33E" stopOpacity={0.3} />
              </linearGradient>
            </defs>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="hour" />
            <YAxis tickFormatter={(value) => `${value} Kw`} />
            <Tooltip formatter={(value) => `${value} Kw`} />
            <Legend />

            {/* Separate bars for each energy type */}
            <Bar
              dataKey="energyDemand"
              fill="url(#energyDemandGradient)"
              name="Energy Demand"
            />
            <Bar
              dataKey="energyProduced"
              fill="url(#energyProducedGradient)"
              name="Energy Produced"
            />
            <Bar
              dataKey="energyTraded"
              fill="url(#energyTradedGradient)"
              name="Energy Traded"
            />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default EnergyBarChart;
