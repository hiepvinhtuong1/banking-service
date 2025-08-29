import { Routes, Route, Navigate, Outlet } from "react-router-dom";
import Auth from "~/pages/Auth/Auth";
import NotFound from "~/pages/404/NotFound";
import AccountVerification from "~/pages/Auth/AccountVerification";
import { useDispatch, useSelector } from "react-redux";
import { selectCurrentUser } from "~/redux/user/userSlice";
import {
	selectGlobalError,
	clearGlobalError,
} from "~/redux/error404/errorSlice";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import MainLayout from "~/layouts/MainLayout";
import Dashboard from "~/pages/Dashboard/Dashboard";
import AccountManagement from "~/pages/Account/AccountManagement";
const ProtectedRoute = ({ user }) => {
	if (!user) {
		return <Navigate to="/login" replace="true" />;
	}
	return <Outlet />;
};

function App() {
	const currentUser = useSelector(selectCurrentUser);
	const globalError = useSelector(selectGlobalError);
	const navigate = useNavigate();
	const dispatch = useDispatch();

	useEffect(() => {
		if (globalError === 404) {
			navigate("/*", { replace: true });
			dispatch(clearGlobalError()); // clear sau khi redirect để tránh lặp
		}
	}, [globalError, navigate, dispatch]);
	return (
		<Routes>
			{/* Redirect Route */}
			<Route element={<ProtectedRoute user={currentUser} />}>
				<Route path="/" element={<MainLayout />}>
					<Route index element={<Dashboard />} />
					<Route path="account" element={<AccountManagement />} />
					<Route path="card" element={<div>Card Page</div>} />
					<Route
						path="transaction"
						element={<div>Transaction Page</div>}
					/>
					<Route
						path="userlevel"
						element={<div>UserLevel Page</div>}
					/>
				</Route>
			</Route>
			{/* Authentication */}
			<Route path="/login" element={<Auth />} />
			<Route path="/register" element={<Auth />} />
			<Route
				path="/account/verification"
				element={<AccountVerification />}
			/>
			{/* 404 not found page */}
			<Route path="*" element={<NotFound />} />
		</Routes>
	);
}

export default App;
