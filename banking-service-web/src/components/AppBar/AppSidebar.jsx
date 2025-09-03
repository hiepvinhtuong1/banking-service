import {
	Drawer,
	List,
	ListItemButton,
	ListItemIcon,
	ListItemText,
	Toolbar,
} from "@mui/material";
import DashboardIcon from "@mui/icons-material/Dashboard";
import PeopleIcon from "@mui/icons-material/People";
import CreditCardIcon from "@mui/icons-material/CreditCard";
import SwapHorizIcon from "@mui/icons-material/SwapHoriz";
import StarIcon from "@mui/icons-material/Star";
import { Link } from "react-router-dom";

const menuItems = [
	{ text: "Dashboard", icon: <DashboardIcon />, path: "/" },
	{ text: "Account", icon: <PeopleIcon />, path: "/account" },
	{ text: "Card", icon: <CreditCardIcon />, path: "/card" },
	{ text: "Transaction", icon: <SwapHorizIcon />, path: "/transaction" },
	{ text: "User Level", icon: <StarIcon />, path: "/user-level" },
];

export default function AppSidebar() {
	return (
		<Drawer
			variant="permanent"
			sx={{
				width: 240,
				flexShrink: 0,
				[`& .MuiDrawer-paper`]: { width: 240, boxSizing: "border-box" },
			}}
		>
			<Toolbar />
			<List>
				{menuItems.map((item) => (
					<ListItemButton
						component={Link}
						to={item.path}
						key={item.text}
					>
						<ListItemIcon>{item.icon}</ListItemIcon>
						<ListItemText primary={item.text} />
					</ListItemButton>
				))}
			</List>
		</Drawer>
	);
}
