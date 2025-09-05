import { AppBar, Toolbar, Typography, Button, Box } from "@mui/material";
import { Link, useNavigate } from "react-router-dom";
import Profile from "./Profile";

function AppTopBarClient() {
	const navigate = useNavigate();

	return (
		<AppBar position="static" color="default" elevation={1}>
			<Toolbar sx={{ justifyContent: "space-between" }}>
				{/* Logo */}
				<Typography
					variant="h6"
					component="div"
					sx={{ cursor: "pointer", fontWeight: "bold" }}
					onClick={() => navigate("/")}
				>
					MyBank
				</Typography>

				{/* Menu */}
				<Box sx={{ display: "flex", gap: 3 }}>
					<Button component={Link} to="/" color="inherit">
						Trang Chủ
					</Button>
					<Button
						component={Link}
						to="/app/my-account"
						color="inherit"
					>
						Tài Khoản
					</Button>
					<Button component={Link} to="/app/history" color="inherit">
						Lịch sử
					</Button>
					<Button component={Link} to="app/services" color="inherit">
						Dịch Vụ
					</Button>
					<Button component={Link} to="/contact" color="inherit">
						Liên Hệ
					</Button>
				</Box>

				<Box>
					<Profile />
				</Box>
			</Toolbar>
		</AppBar>
	);
}

export default AppTopBarClient;
