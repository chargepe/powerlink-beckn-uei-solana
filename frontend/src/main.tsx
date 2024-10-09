import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { Provider } from "react-redux";
import App from "./App.tsx";
import "./index.css";
import { GoogleOAuthProvider } from "@react-oauth/google";
import { store, persistor } from "./redux/store.ts";
import { PersistGate } from "redux-persist/integration/react";


createRoot(document.getElementById("root")!).render(
  <Provider store={store}>
    <PersistGate loading={null} persistor={persistor}>
      <GoogleOAuthProvider clientId={import.meta.env.VITE_GOOGLE_CLIENT_ID}>
        <StrictMode>
          <App />
        </StrictMode>
      </GoogleOAuthProvider>
    </PersistGate>
  </Provider>
);
