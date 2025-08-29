// src/pages/Admin/AccountManagement.jsx
import { Card, Box, Button, Stack } from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import { mockAccounts } from "~/apis/mockAccount";

export default function AccountManagement() {
	const columns = [
		{
			field: "customerName",
			headerName: "Customer Name",
			flex: 1,
			minWidth: 150,
		},
		{ field: "email", headerName: "Email", flex: 1, minWidth: 200 },
		{
			field: "phoneNumber",
			headerName: "Phone Number",
			flex: 1,
			minWidth: 150,
		},

		{
			field: "active",
			headerName: "Status",
			flex: 1,
			minWidth: 120,
			renderCell: (params) =>
				params.value ? (
					<span style={{ color: "green", fontWeight: "bold" }}>
						Active
					</span>
				) : (
					<span style={{ color: "red", fontWeight: "bold" }}>
						Inactive
					</span>
				),
		},
		{
			field: "actions",
			headerName: "Actions",
			sortable: false,
			flex: 1,
			minWidth: 200,
			renderCell: () => (
				<Stack
					direction="row"
					spacing={1}
					sx={{
						display: "flex",
						justifyContent: "center", // căn ngang
						alignItems: "center", // căn dọc
						height: "100%", // để Stack chiếm full chiều cao của cell
						width: "100%",
						"& button": {
							borderRadius: "8px",
							textTransform: "none",
							fontSize: "0.8rem",
						},
					}}
				>
					<Button size="small" variant="outlined" color="primary">
						View
					</Button>
					<Button size="small" variant="outlined" color="warning">
						Update
					</Button>
					<Button size="small" variant="outlined" color="error">
						Delete
					</Button>
				</Stack>
			),
		},
	];

	return (
		<Card sx={{ p: 2 }}>
			<h2 className="text-xl font-semibold mb-4">Account Management</h2>
			<Box
				sx={{
					width: "100%",
					overflowX: "auto",
				}}
			>
				<DataGrid
					rows={mockAccounts}
					columns={columns}
					getRowId={(row) => row.accountId}
					autoHeight
					pageSizeOptions={[5, 10]}
					initialState={{
						pagination: {
							paginationModel: { pageSize: 10, page: 0 },
						},
					}}
				/>
			</Box>
		</Card>
	);
}
