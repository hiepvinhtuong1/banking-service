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
import Dashboard from "~/pages/admin/Dashboard/Dashboard";
import Accounts from "~/pages/admin/Accounts/Accounts";
import CreateAccountForm from "~/pages/admin/Accounts/CreateAccountForm";
import UpdateAccountForm from "./pages/admin/Accounts/UpdateAccountForm";
import UserLevels from "./pages/admin/UserLevel/UserLevels";
import CreateUserLevelForm from "./pages/admin/UserLevel/CreateUserLevelForm";
import Account from "./pages/admin/Accounts/Account";
import CreateCardForm from "./pages/admin/Cards/CreateCardForm";
import Cards from "./pages/admin/Cards/Cards";
import CardDetail from "./pages/admin/Cards/Card";
import ClientLayout from "./layouts/ClientLayout";
import HomePage from "./pages/client/HomePage";
import MyAccount from "./pages/client/MyAccount";
import TransferPage from "./pages/client/TranferPage";
import Transactions from "./pages/admin/transactions/Transactions";
const ProtectedRoute = ({ user, roles }) => {
	if (!user) {
		// Chưa đăng nhập → về login
		return <Navigate to="/login" replace />;
	}

	if (roles && !roles.some((r) => user.roles?.includes(r))) {
		// Có roles yêu cầu nhưng user không có → unauthorized
		return <Navigate to="/unauthorized" replace />;
	}

	// Đủ điều kiện → render con
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
			dispatch(clearGlobalError());
		}
	}, [globalError, navigate, dispatch]);

	return (
		<Routes>
			{/*
		==============================
		🔒 Protected Admin Routes
		- Chỉ user có role = ADMIN mới truy cập
		==============================
	*/}
			<Route
				element={
					<ProtectedRoute user={currentUser} roles={["ADMIN"]} />
				}
			>
				<Route path="/admin" element={<MainLayout />}>
					{/* Dashboard */}
					<Route index element={<Dashboard />} />

					{/* Account Management */}
					<Route path="account" element={<Accounts />} />
					<Route path="account/:accountId" element={<Account />} />
					<Route
						path="account/create"
						element={<CreateAccountForm />}
					/>
					<Route
						path="account/update/:accountId"
						element={<UpdateAccountForm />}
					/>

					{/* Card Management */}
					<Route path="card" element={<Cards />} />
					<Route path="card/:cardId" element={<CardDetail />} />
					<Route path="card/new" element={<CreateCardForm />} />

					{/* Transaction (chưa implement) */}
					<Route path="transaction" element={<Transactions />} />

					{/* User Levels */}
					<Route path="user-level" element={<UserLevels />} />
					<Route
						path="user-level/create"
						element={<CreateUserLevelForm />}
					/>
				</Route>
			</Route>

			{/*
		==============================
		👤 Protected Client Routes
		- Chỉ user có role = USER mới truy cập
		==============================
	*/}
			<Route
				element={<ProtectedRoute user={currentUser} roles={["USER"]} />}
			>
				<Route path="/app" element={<ClientLayout />}>
					<Route index element={<HomePage />} />
					<Route path="my-account" element={<MyAccount />} />
					<Route path="transfer/:cardId" element={<TransferPage />} />
				</Route>
			</Route>

			{/*
		==============================
		🔑 Authentication Routes
		- Đăng nhập, đăng ký, xác minh tài khoản
		==============================
	*/}
			<Route path="/login" element={<Auth />} />
			<Route path="/register" element={<Auth />} />
			<Route
				path="/account/verification"
				element={<AccountVerification />}
			/>

			{/*
		==============================
		🚫 Unauthorized Page
		- Hiện khi user có login nhưng không đủ quyền
		==============================
	*/}
			<Route
				path="/unauthorized"
				element={<div>Không có quyền truy cập</div>}
			/>

			{/*
		==============================
		❌ 404 Not Found
		- Bất kỳ route nào không khớp
		==============================
	*/}
			<Route path="*" element={<NotFound />} />
		</Routes>
	);
}

export default App;
