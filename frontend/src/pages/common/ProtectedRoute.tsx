import { FC, ReactNode } from "react";
import { Navigate } from "react-router-dom";

interface IProtectedRouteProps {
  validate: () => boolean;
  fallbackRoute: string;
  children: ReactNode;
}
const ProtectedRoute: FC<IProtectedRouteProps> = ({
  validate,
  fallbackRoute,
  children,
}) => {
  const isValid = validate();
  if (!isValid) {
    return <Navigate to={fallbackRoute} />;
  }
  return <>{children}</>;
};

export default ProtectedRoute;
