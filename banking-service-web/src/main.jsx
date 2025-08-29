import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import App from "./App.jsx";

//cấu hình react-router-dom với BrowserRouter
import { BrowserRouter } from "react-router-dom";
import { CssBaseline, GlobalStyles } from "@mui/material";
import { injectStore } from "./utils/authorizeAxios.js";
import { store } from "~/redux/store";

// Cấu hình Redux-Persist
import { PersistGate } from "redux-persist/integration/react";
import { persistStore } from "redux-persist";
import { Provider } from "react-redux";
import { ToastContainer } from "react-toastify";
import { ConfirmProvider } from "material-ui-confirm";

// Inject store vào authorizedAxiosInstance
injectStore(store);

const persistor = persistStore(store);

createRoot(document.getElementById("root")).render(
	<BrowserRouter basename="/">
		<Provider store={store}>
			<PersistGate loading={null} persistor={persistor}>
				<ConfirmProvider
					defaultOptions={{
						allowClose: false,
						dialogProps: { maxWidth: "xs" },
						confirmationButtonProps: { color: "success" },
						cancellationButtonProps: { color: "error" },
					}}
				>
					<GlobalStyles styles={{ a: { textDecoration: "none" } }} />
					<CssBaseline />
					<App />
					<ToastContainer position="bottom-right" />
				</ConfirmProvider>
			</PersistGate>
		</Provider>
	</BrowserRouter>
);
