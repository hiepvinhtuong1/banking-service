import { useEffect, useState } from "react";
import {
	Container,
	Box,
	Typography,
	Stack,
	Button,
	TextField,
	Table,
	TableBody,
	TableCell,
	TableContainer,
	TableHead,
	TableRow,
	Paper,
	Pagination,
	Breadcrumbs,
	Chip,
	PaginationItem,
} from "@mui/material";
import { fetchAccountAPI } from "~/apis";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { DEFAULT_ITEMS_PER_PAGE, DEFAULT_PAGE } from "~/utils/constant";

function Accounts() {
	const [accounts, setAccounts] = useState(null);
	const [totalAccounts, setTotalAccounts] = useState(0);
	const navigate = useNavigate();
	const location = useLocation();

	// Lấy page từ query
	const query = new URLSearchParams(location.search);
	const page = parseInt(query.get("page") || "1", 10);

	const handleSearchName = (event) => {
		// Bắt hành động người dùng nhấn phím Enter && không phải hành động Shift + Enter
		if (event.key === "Enter" && !event.shiftKey) {
			event.preventDefault(); // Thêm dòng này để khi Enter không bị nhảy dòng

			// Set lại customerName
			query.set("customerName", event.target?.value);

			// Reset page về 1 khi search mới
			query.set("page", DEFAULT_PAGE);

			query.set("size", DEFAULT_ITEMS_PER_PAGE);

			// Điều hướng sang URL mới
			navigate(`/account?${query.toString()}`);
		}
	};

	useEffect(() => {
		fetchAccountAPI(location.search).then((res) => {
			console.log("🚀 ~ Accounts ~ res:", res);
			setAccounts(res?.content || []);
			setTotalAccounts(res?.totalElements || 0);
		});
	}, [location.search]);

	return (
		<Container disableGutters maxWidth={false}>
			<Box sx={{ paddingX: 2, my: 4 }}>
				{/* Breadcrumb */}
				<Breadcrumbs sx={{ mb: 2 }}>
					<Link underline="hover" color="inherit" href="/dashboard">
						Dashboard
					</Link>
					<Typography color="text.primary">Account</Typography>
				</Breadcrumbs>

				{/* Title + Create Button */}
				<Stack
					direction="row"
					alignItems="center"
					justifyContent="space-between"
					sx={{ mb: 2 }}
				>
					<Typography variant="h5" fontWeight="bold">
						Table Account
					</Typography>
					<Button
						variant="contained"
						color="primary"
						onClick={() => navigate("/account/create")}
					>
						Create a account
					</Button>
				</Stack>

				{/* Search */}
				<Box
					sx={{ mb: 2, display: "flex", justifyContent: "flex-end" }}
				>
					<TextField
						placeholder="Search by customer name"
						size="small"
						sx={{ width: 300 }}
						onKeyDown={handleSearchName}
					/>
				</Box>

				{/* Table */}
				<TableContainer component={Paper}>
					<Table>
						<TableHead>
							<TableRow>
								<TableCell>STT</TableCell>
								<TableCell>Customer name</TableCell>
								<TableCell>Email</TableCell>
								<TableCell>ROLE</TableCell>
								<TableCell>Level</TableCell>
								<TableCell align="center">Actions</TableCell>
							</TableRow>
						</TableHead>
						<TableBody>
							{accounts?.map((account, index) => (
								<TableRow key={account?.accountId}>
									<TableCell>
										{(page - 1) * DEFAULT_ITEMS_PER_PAGE +
											index +
											1}
									</TableCell>
									<TableCell>
										{account?.customerName}
									</TableCell>
									<TableCell>{account?.email}</TableCell>
									{/* Roles */}
									<TableCell>
										<Stack direction="row" spacing={1}>
											{account?.roles?.map(
												(role, idx) => (
													<Chip
														key={idx}
														label={role.name}
														color="info"
														size="small"
													/>
												)
											)}
										</Stack>
									</TableCell>

									<TableCell>
										{account?.userLevel?.name}
									</TableCell>
									<TableCell align="center">
										<Stack
											direction="row"
											spacing={1}
											justifyContent="center"
										>
											<Button
												variant="contained"
												color="success"
												size="small"
												onClick={() =>
													navigate(
														`/account/${account?.accountId}`
													)
												}
											>
												View Account
											</Button>
											<Button
												variant="contained"
												color="warning"
												size="small"
												onClick={() =>
													navigate(
														`/account/update/${account?.accountId}`
													)
												}
											>
												Update
											</Button>

											<Button
												variant="contained"
												color="error"
												size="small"
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

				{/* Pagination */}
				<Box sx={{ mt: 3, display: "flex", justifyContent: "center" }}>
					<Pagination
						showFirstButton
						showLastButton
						count={Math.ceil(
							totalAccounts / DEFAULT_ITEMS_PER_PAGE
						)}
						page={page}
						color="primary"
						// Render các page item và đồng thời cũng là những cái link để chúng ta click chuyển trang
						renderItem={(item) => (
							<PaginationItem
								component={Link}
								to={`/account${
									item.page === DEFAULT_PAGE
										? `?size=${DEFAULT_ITEMS_PER_PAGE}`
										: `?page=${item.page}&size=${DEFAULT_ITEMS_PER_PAGE}`
								}`}
								{...item}
							/>
						)}
					/>
				</Box>
			</Box>
		</Container>
	);
}

export default Accounts;
