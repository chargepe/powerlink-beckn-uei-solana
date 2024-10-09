import { Outlet } from "react-router-dom";
import Header from "./common/Header";
import { Toaster } from "@/components/ui/toaster";

const Main = () => {
  return (
    <div className="p-0 m-0">
      <Toaster />
      <Header />
      <div className="overflow-y-auto flex grow">
        <Outlet />
      </div>
    </div>
  );
};

export default Main;
