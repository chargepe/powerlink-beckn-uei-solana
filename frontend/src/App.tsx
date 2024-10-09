import { createBrowserRouter, RouterProvider } from "react-router-dom";
import "./App.css";
import RegisterLogin from "./pages/registerLogin.tsx";
import ProtectedRoute from "./pages/common/ProtectedRoute.tsx";
import { getItem } from "./utils/config.ts";
import Main from "./pages";
import { ROUTE_ANALYTICS, ROUTE_HOME, ROUTE_REGISTER } from "./utils/routes.ts";
import AnalyticsPage from "./pages/grid/AnalyticsPage.tsx";
import Marketplace from "./pages/marketplace/index.tsx";

export default function App() {
  const router = createBrowserRouter([
    {
      path: ROUTE_HOME,
      element: <Main/>,
      children: [
        {
          path: ROUTE_HOME,
          element: (
            <ProtectedRoute
              validate={() => !!getItem("profile")}
              fallbackRoute={ROUTE_REGISTER}
            >
              <Marketplace />
            </ProtectedRoute>
          ),
        },
        {
          path: ROUTE_ANALYTICS,
          element: (
            <ProtectedRoute
              validate={() => getItem("type") === "admin"}
              fallbackRoute={ROUTE_HOME}
            >
              <AnalyticsPage />
            </ProtectedRoute>
          ),
        },
      ],
    },
    {
      path: ROUTE_REGISTER,
      element: (
        <ProtectedRoute
          validate={() => !getItem("profile")}
          fallbackRoute={ROUTE_HOME}
        >
          <RegisterLogin />
        </ProtectedRoute>
      ),
    },
  ]);

  return (
    <>
      <RouterProvider router={router} />
    </>
  );
}
