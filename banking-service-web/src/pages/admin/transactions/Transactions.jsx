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
	Pagination,
	PaginationItem,
	Breadcrumbs,
	TextField,
	MenuItem,
} from "@mui/material";
import { v4 as uuidv4 } from "uuid";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { DEFAULT_ITEMS_PER_PAGE, DEFAULT_PAGE } from "~/utils/constant";
import {
	fetchTransactionsAPI,
	fetchTransactionTypesAPI,
	fetchTransactionStatusAPI,
	approveTransactionAPI,
	rejectTransactionAPI,
} from "~/apis";

function Transactions() {
	const [transactions, setTransactions] = useState(null);
	const [totalTransactions, setTotalTransactions] = useState(0);
	const [transactionTypes, setTransactionTypes] = useState([]); // State cho danh sách Transaction Type
	const [transactionStatuses, setTransactionStatuses] = useState([]); // State cho danh sách Transaction Status
	const navigate = useNavigate();
	const location = useLocation();

	// Lấy page, transactionId, transactionType, và transactionStatus từ query
	const query = new URLSearchParams(location.search);
	const page = parseInt(query.get("page") || "1", 10);
	const transactionId = query.get("transactionId") || "";
	const transactionType = query.get("transactionType") || "";
	const transactionStatus = query.get("transactionStatus") || "";

	const handleSearchTransactionId = (event) => {
		if (event.key === "Enter" && !event.shiftKey) {
			event.preventDefault();
			query.set("transactionId", event.target.value);
			query.set("page", DEFAULT_PAGE);
			query.set("size", DEFAULT_ITEMS_PER_PAGE);
			navigate(`/admin/transaction?${query.toString()}`);
		}
	};

	const handleFilterTransactionType = (event) => {
		const value = event.target.value;
		if (value === "") {
			query.delete("transactionType"); // Xóa tham số nếu giá trị rỗng
		} else {
			query.set("transactionType", value); // Đặt tham số nếu có giá trị
		}
		query.set("page", DEFAULT_PAGE);
		query.set("size", DEFAULT_ITEMS_PER_PAGE);
		navigate(`/admin/transaction?${query.toString()}`);
	};

	const handleFilterTransactionStatus = (event) => {
		const value = event.target.value;
		if (value === "") {
			query.delete("transactionStatus"); // Xóa tham số nếu giá trị rỗng
		} else {
			query.set("transactionStatus", value); // Đặt tham số nếu có giá trị
		}
		query.set("page", DEFAULT_PAGE);
		query.set("size", DEFAULT_ITEMS_PER_PAGE);
		navigate(`/admin/transaction?${query.toString()}`);
	};

	useEffect(() => {
		// Gọi API để lấy danh sách Transaction Types
		fetchTransactionTypesAPI().then((data) => {
			setTransactionTypes(data);
		});

		// Gọi API để lấy danh sách Transaction Statuses
		fetchTransactionStatusAPI().then((data) => {
			setTransactionStatuses(data);
		});

		// Gọi API để lấy danh sách giao dịch
		fetchTransactionsAPI(location.search).then((res) => {
			setTransactions(res?.content || []);
			setTotalTransactions(res?.totalElements || 0);
		});
	}, [
		location.search,
		transactionId,
		transactionType,
		transactionStatus,
		navigate,
	]);

	return (
		<Container disableGutters maxWidth={false}>
			<Box sx={{ paddingX: 2, my: 4 }}>
				{/* Breadcrumb */}
				<Breadcrumbs sx={{ mb: 2 }}>
					<Link underline="hover" color="inherit" href="/dashboard">
						Dashboard
					</Link>
					<Typography color="text.primary">Transactions</Typography>
				</Breadcrumbs>

				{/* Title + Create Button */}
				<Stack
					direction="row"
					alignItems="center"
					justifyContent="space-between"
					sx={{ mb: 2 }}
				>
					<Typography variant="h5" fontWeight="bold">
						Table Transactions
					</Typography>
					<Button
						variant="contained"
						color="primary"
						onClick={() => navigate("/transaction/create")}
					>
						Create a transaction
					</Button>
				</Stack>

				{/* Search and Filters */}
				<Box
					sx={{
						mb: 2,
						display: "flex",
						justifyContent: "flex-start",
						gap: 2,
					}}
				>
					<TextField
						select
						label="Transaction Type"
						size="small"
						sx={{ width: 200 }}
						value={transactionType}
						onChange={handleFilterTransactionType}
					>
						<MenuItem value="">All</MenuItem>
						{transactionTypes.map((type) => (
							<MenuItem key={type} value={type}>
								{type}
							</MenuItem>
						))}
					</TextField>
					<TextField
						select
						label="Transaction Status"
						size="small"
						sx={{ width: 200 }}
						value={transactionStatus}
						onChange={handleFilterTransactionStatus}
					>
						<MenuItem value="">All</MenuItem>
						{transactionStatuses.map((status) => (
							<MenuItem key={status} value={status}>
								{status}
							</MenuItem>
						))}
					</TextField>
					<TextField
						placeholder="Search by Transaction ID"
						size="small"
						sx={{ width: 300 }}
						defaultValue={transactionId}
						onKeyDown={handleSearchTransactionId}
					/>
				</Box>

				{/* Table */}
				<TableContainer
					component={Paper}
					sx={{ overflowX: "auto", minWidth: 1500 }}
				>
					<Table>
						<TableHead>
							<TableRow>
								<TableCell sx={{ width: 50 }}>STT</TableCell>
								<TableCell sx={{ width: 250 }}>ID</TableCell>
								<TableCell sx={{ width: 150 }}>
									From Card Number
								</TableCell>
								<TableCell sx={{ width: 150 }}>
									To Card Number
								</TableCell>
								<TableCell sx={{ width: 100 }}>
									Amount
								</TableCell>
								<TableCell sx={{ width: 150 }}>Type</TableCell>
								<TableCell sx={{ width: 100 }}>
									Status
								</TableCell>
								<TableCell sx={{ width: 150 }}>
									Created At
								</TableCell>
								<TableCell sx={{ width: 150 }}>
									Updated At
								</TableCell>
								<TableCell
									sx={{ width: 150, textAlign: "center" }}
								>
									Actions
								</TableCell>
							</TableRow>
						</TableHead>
						<TableBody>
							{transactions?.map((transaction, index) => (
								<TableRow key={transaction?.transactionId}>
									<TableCell sx={{ width: 50 }}>
										{(page - 1) * DEFAULT_ITEMS_PER_PAGE +
											index +
											1}
									</TableCell>
									<TableCell sx={{ width: 250 }}>
										{transaction?.transactionId}
									</TableCell>
									<TableCell sx={{ width: 150 }}>
										{transaction?.fromCardNumber}
									</TableCell>
									<TableCell sx={{ width: 150 }}>
										{transaction?.toCardNumber}
									</TableCell>
									<TableCell sx={{ width: 100 }}>
										{transaction?.amount}
									</TableCell>
									<TableCell sx={{ width: 150 }}>
										{transaction?.transactionType}
									</TableCell>
									<TableCell sx={{ width: 100 }}>
										{transaction?.transactionStatus}
									</TableCell>
									<TableCell sx={{ width: 150 }}>
										{transaction?.createdAt}
									</TableCell>
									<TableCell sx={{ width: 150 }}>
										{transaction?.updatedAt}
									</TableCell>
									<TableCell
										sx={{ width: 200, textAlign: "center" }}
									>
										<Stack
											direction="row"
											spacing={1}
											justifyContent="center"
										>
											<Button
												variant="contained"
												color="success"
												size="small"
												onClick={async () => {
													await approveTransactionAPI(
														transaction.transactionId
													);
													// Refresh lại danh sách
													fetchTransactionsAPI(
														location.search
													).then((res) => {
														setTransactions(
															res?.content || []
														);
														setTotalTransactions(
															res?.totalElements ||
																0
														);
													});
												}}
											>
												Approve
											</Button>
											<Button
												variant="contained"
												color="error"
												size="small"
												onClick={async () => {
													await rejectTransactionAPI(
														transaction.transactionId
													);
													// Refresh lại danh sách
													fetchTransactionsAPI(
														location.search
													).then((res) => {
														setTransactions(
															res?.content || []
														);
														setTotalTransactions(
															res?.totalElements ||
																0
														);
													});
												}}
											>
												Reject
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
							totalTransactions / DEFAULT_ITEMS_PER_PAGE
						)}
						page={page}
						color="primary"
						renderItem={(item) => (
							<PaginationItem
								component={Link}
								to={`admin/transaction${
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

export default Transactions;
