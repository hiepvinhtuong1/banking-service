import { Outlet } from "react-router-dom";
import { Box, Container, Typography } from "@mui/material";
import AppTopBarClient from "~/components/AppBar/AppTopBarClient";

function ClientLayout() {
	return (
		<Box display="flex" flexDirection="column" minHeight="100vh">
			{/* Topbar (header) */}
			<AppTopBarClient />

			{/* Nội dung */}
			<Box component="main" flexGrow={1} py={6}>
				<Container maxWidth="lg">
					<Outlet />
				</Container>
			</Box>

			{/* Footer */}
			<Box
				component="footer"
				sx={{
					bgcolor: "grey.100",
					textAlign: "center",
					py: 2,
					borderTop: 1,
					borderColor: "grey.300",
				}}
			>
				<Typography variant="body2" color="text.secondary">
					© {new Date().getFullYear()} MyBank - All rights reserved.
				</Typography>
			</Box>
		</Box>
	);
}

export default ClientLayout;
