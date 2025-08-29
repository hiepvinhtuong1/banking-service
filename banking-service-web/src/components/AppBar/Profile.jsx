import { useState } from "react";
import Button from "@mui/material/Button";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import Divider from "@mui/material/Divider";
import ListItemIcon from "@mui/material/ListItemIcon";
import Box from "@mui/material/Box";
import Avatar from "@mui/material/Avatar";
import Tooltip from "@mui/material/Tooltip";
import IconButton from "@mui/material/IconButton";
import PersonAdd from "@mui/icons-material/PersonAdd";
import Settings from "@mui/icons-material/Settings";
import Logout from "@mui/icons-material/Logout";
import { useDispatch, useSelector } from "react-redux";
import { selectCurrentUser, logoutUserAPI } from "~/redux/user/userSlice";
import { useConfirm } from "material-ui-confirm";
import { Link } from "react-router-dom";
function Profile() {
	const [anchorEl, setAnchorEl] = useState(null);
	const open = Boolean(anchorEl);
	const handleClick = (event) => {
		setAnchorEl(event.currentTarget);
	};
	const handleClose = () => {
		setAnchorEl(null);
	};

	const dispatch = useDispatch();
	const currentUser = useSelector(selectCurrentUser);

	const confirmLogout = useConfirm();

	const handleLogout = () => {
		confirmLogout({
			title: "Log out of your account",
			confirmationText: "Confirm",
			cancellationText: "Cancel",
		})
			.then(() => {
				dispatch(logoutUserAPI());
			})
			.catch(() => {});
	};
	return (
		<div>
			<Box>
				<Tooltip title="Account settings">
					<IconButton
						onClick={handleClick}
						size="small"
						sx={{ padding: 0 }}
						aria-controls={open ? "basic-menu-profiles" : undefined}
						aria-haspopup="true"
						aria-expanded={open ? "true" : undefined}
					>
						<Avatar
							alt="Tuan Hiep Bui"
							src={currentUser?.avatar}
							sx={{ width: 36, height: 36 }}
						/>
					</IconButton>
				</Tooltip>
				<Menu
					id="basic-menu-profile"
					anchorEl={anchorEl}
					open={open}
					onClose={handleClose}
					onClick={handleClose}
					slotProps={{
						list: {
							"aria-labelledby": "basic-button-profile",
						},
					}}
				>
					<Link
						to="/settings/account"
						style={{
							color: "inherit",
						}}
					>
						<MenuItem
							sx={{
								"&:hover": { color: "success.light" },
							}}
						>
							<Avatar
								src={currentUser?.avatar}
								sx={{ width: 28, height: 28, mr: 2 }}
							/>{" "}
							Profile
						</MenuItem>
					</Link>

					<Divider />
					<MenuItem>
						<ListItemIcon>
							<PersonAdd fontSize="small" />
						</ListItemIcon>
						Add another account
					</MenuItem>
					<MenuItem>
						<ListItemIcon>
							<Settings fontSize="small" />
						</ListItemIcon>
						Settings
					</MenuItem>
					<MenuItem
						onClick={handleLogout}
						sx={{
							"&:hover": {
								color: "warning.dark",
								"& .logout-icon": {
									color: "warning.dark",
								},
							},
						}}
					>
						<ListItemIcon>
							<Logout className="logout-icon" fontSize="small" />
						</ListItemIcon>
						Logout
					</MenuItem>
				</Menu>
			</Box>
		</div>
	);
}

export default Profile;
