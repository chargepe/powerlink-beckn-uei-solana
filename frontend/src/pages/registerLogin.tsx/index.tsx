import { useState, useEffect } from "react";
import { useGoogleLogin } from "@react-oauth/google";
import axios from "axios";
import { useToast } from "@/hooks/use-toast"
import RegisterPage from "./RegisterPage";
import { getItem, setItem } from "@/utils/config";
import { useNavigate } from "react-router-dom";
import { ROUTE_HOME } from "@/utils/routes";
import services from "@/services/services";
import { useAppDispatch, useAppSelector } from "@/redux/hooks";
import { updateUser } from "@/redux/slices/userSlice";

function RegisterLogin() {
  const [user, setUser] = useState<any>(null);
  const [profile, setProfile] = useState(null);
  const [userExist, setUserExist] = useState(false);
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const {wallet} = useAppSelector(state=>state.user);
  const { toast } = useToast()

  const login = useGoogleLogin({
    onSuccess: (codeResponse) => setUser(codeResponse),
    onError: (error) => console.log("Login Failed:", error),
  });

  useEffect(() => {
    if (user) {
      axios
        .get(
          `https://www.googleapis.com/oauth2/v1/userinfo?access_token=${user.access_token}`,
          {
            headers: {
              Authorization: `Bearer ${user?.access_token}`,
              Accept: "application/json",
            },
          }
        )
        .then((res) => {
          setItem("profile", res.data);
          setProfile(res.data);
        })
        .catch((err) => console.log(err));
    }
  }, [user]);

  const register = async (body: {
    name: any;
    email: any;
    energyExchangeCapability: string;
    industryType: string;
    initialBalance: number;
    gridId: number;
  }) => {
    try {
      const data = await services.registerUser(body);
      const solBalance = data?.wallet?.solPerRupee*data?.wallet?.balance;
      toast({
        title: `solPerRupee value on app: ${wallet?.solPerRupee}`,
        description: `We have added ${solBalance} SOL for test purpose.`,
      })
      dispatch(updateUser(data));
    } catch (error) {
      console.log(error);
    }
  };

  const checkIfUserExist = async (email: string) => {
    try {
      const data = await services.getUerExist(email);
      if (data?.id) {
        const solBalance = data?.wallet?.solPerRupee*data?.wallet?.balance;
        toast({
          title: `solPerRupee value on app: ${wallet?.solPerRupee}`,
          description: `We have added ${solBalance} SOL for test purpose.`,
        })
        dispatch(updateUser(data));
        setUserExist(true);
      }
    } catch (error) {
      setUserExist(false);
    }
  };

  useEffect(() => {
    if (!!getItem("profile")) {
      const profile = getItem("profile");
      const body = {
        name: profile?.name,
        email: profile?.email,
        energyExchangeCapability: "SELLER",
        industryType: "PERSONAL",
        initialBalance: 1000.0,
        gridId: 1,
      };
      checkIfUserExist(body.email);
      if (!userExist) {
        register(body);
      }

      navigate(ROUTE_HOME);
    }
  }, [profile]);

  return <RegisterPage onClick={() => login()} />;
}

export default RegisterLogin;
