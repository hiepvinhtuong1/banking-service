import { useEffect, useState } from "react";
import {
	Container,
	Box,
	Typography,
	Stack,
	Button,
	Table,
	TableBody,
	TableCell,
	TableContainer,
	TableHead,
	TableRow,
	Paper,
	Breadcrumbs,
} from "@mui/material";
import { fetchUserLevelAPI } from "~/apis"; // 👈 bạn tạo API gọi backend lấy userLevels
import { Link, useNavigate } from "react-router-dom";

function UserLevels() {
	const [userLevels, setUserLevels] = useState([]);
	const navigate = useNavigate();

	useEffect(() => {
		fetchUserLevelAPI().then((res) => {
			setUserLevels(res || []);
		});
	}, []);

	return (
		<Container disableGutters maxWidth={false}>
			<Box sx={{ paddingX: 2, my: 4 }}>
				{/* Breadcrumb */}
				<Breadcrumbs sx={{ mb: 2 }}>
					<Link underline="hover" color="inherit" to="/dashboard">
						Dashboard
					</Link>
					<Typography color="text.primary">User Level</Typography>
				</Breadcrumbs>

				{/* Title + Create Button */}
				<Stack
					direction="row"
					alignItems="center"
					justifyContent="space-between"
					sx={{ mb: 2 }}
				>
					<Typography variant="h5" fontWeight="bold">
						User Levels List
					</Typography>
					<Button
						variant="contained"
						color="primary"
						onClick={() => navigate("/admin/user-level/create")}
					>
						Add New User Level
					</Button>
				</Stack>

				{/* Table */}
				<TableContainer component={Paper}>
					<Table>
						<TableHead>
							<TableRow>
								<TableCell>ID</TableCell>
								<TableCell>Level Name</TableCell>
								<TableCell>Card Limit</TableCell>
								<TableCell>Daily Transfer Limit</TableCell>
								<TableCell align="center">Actions</TableCell>
							</TableRow>
						</TableHead>
						<TableBody>
							{userLevels?.map((level) => (
								<TableRow key={level.id}>
									<TableCell>{level.id}</TableCell>
									<TableCell>{level.name}</TableCell>
									<TableCell>{level.numberOfCards}</TableCell>
									<TableCell>
										{level.dailyTransactionAmount.toLocaleString(
											"vi-VN"
										)}
									</TableCell>
									<TableCell align="center">
										<Stack
											direction="row"
											spacing={1}
											justifyContent="center"
										>
											<Button
												variant="contained"
												color="warning"
												size="small"
												onClick={() =>
													navigate(
														`/user-level/update/${level.id}`
													)
												}
											>
												Edit
											</Button>
											<Button
												variant="contained"
												color="error"
												size="small"
												onClick={() =>
													console.log(
														"Delete user level",
														level.id
													)
												}
											>
												Delete
											</Button>
										</Stack>
									</TableCell>
								</TableRow>
							))}
						</TableBody>
					</Table>
				</TableContainer>
			</Box>
		</Container>
	);
}

export default UserLevels;
