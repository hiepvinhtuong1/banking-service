import { Box, Toolbar } from "@mui/material";
import AppSidebar from "~/components/AppBar/AppSidebar";
import AppTopbar from "~/components/AppBar/AppTopbar";
import { Outlet } from "react-router-dom";

export default function MainLayout() {
	return (
		<Box sx={{ display: "flex" }}>
			<AppTopbar />
			<AppSidebar />
			<Box component="main" sx={{ flexGrow: 1, p: 3 }}>
				<Toolbar /> {/* tạo khoảng trống dưới AppBar */}
				<Outlet />
			</Box>
		</Box>
	);
}
