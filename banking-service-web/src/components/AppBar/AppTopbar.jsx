import { AppBar, Toolbar, Typography, Box } from "@mui/material";
import Profile from "./Profile";

export default function AppTopbar() {
	return (
		<AppBar
			position="fixed"
			sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}
		>
			<Toolbar>
				<Typography variant="h6" sx={{ flexGrow: 1 }}>
					Banking Dashboard
				</Typography>

				{/* Profile menu */}
				<Box>
					<Profile />
				</Box>
			</Toolbar>
		</AppBar>
	);
}
