import { useEffect, useState } from "react";
import {
	Container,
	Box,
	Typography,
	Stack,
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
import { Link, useLocation, useNavigate } from "react-router-dom";
import { DEFAULT_ITEMS_PER_PAGE, DEFAULT_PAGE } from "~/utils/constant";
import {
	fetchMyTransactionsAPI,
	fetchTransactionTypesAPI,
	fetchTransactionStatusAPI,
} from "~/apis";

function MyTransactions() {
	const [transactions, setTransactions] = useState(null);
	const [totalTransactions, setTotalTransactions] = useState(0);
	const [transactionTypes, setTransactionTypes] = useState([]);
	const [transactionStatuses, setTransactionStatuses] = useState([]);
	const navigate = useNavigate();
	const location = useLocation();

	// query params
	const query = new URLSearchParams(location.search);
	const page = parseInt(query.get("page") || "1", 10);
	const transactionId = query.get("transactionId") || "";
	const transactionType = query.get("transactionType") || "";
	const transactionStatus = query.get("transactionStatus") || "";

	// Search by Transaction ID
	const handleSearchTransactionId = (event) => {
		if (event.key === "Enter" && !event.shiftKey) {
			event.preventDefault();
			query.set("transactionId", event.target.value);
			query.set("page", DEFAULT_PAGE);
			query.set("size", DEFAULT_ITEMS_PER_PAGE);
			navigate(`/my-transactions?${query.toString()}`);
		}
	};

	// Filter by Type
	const handleFilterTransactionType = (event) => {
		const value = event.target.value;
		if (value === "") query.delete("transactionType");
		else query.set("transactionType", value);
		query.set("page", DEFAULT_PAGE);
		query.set("size", DEFAULT_ITEMS_PER_PAGE);
		navigate(`/my-transactions?${query.toString()}`);
	};

	// Filter by Status
	const handleFilterTransactionStatus = (event) => {
		const value = event.target.value;
		if (value === "") query.delete("transactionStatus");
		else query.set("transactionStatus", value);
		query.set("page", DEFAULT_PAGE);
		query.set("size", DEFAULT_ITEMS_PER_PAGE);
		navigate(`/my-transactions?${query.toString()}`);
	};

	useEffect(() => {
		// fetch transaction types
		fetchTransactionTypesAPI().then(setTransactionTypes);
		// fetch statuses
		fetchTransactionStatusAPI().then(setTransactionStatuses);
		// fetch user's transactions
		fetchMyTransactionsAPI(location.search).then((res) => {
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
				{/* Title */}
				<Stack
					direction="row"
					alignItems="center"
					justifyContent="space-between"
					sx={{ mb: 2 }}
				>
					<Typography variant="h4" fontWeight="bold">
						My Transactions
					</Typography>
				</Stack>

				{/* Search & Filters */}
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
					sx={{ overflowX: "auto", minWidth: 1200 }}
				>
					<Table>
						<TableHead>
							<TableRow>
								<TableCell sx={{ width: 50 }}>STT</TableCell>
								<TableCell sx={{ width: 250 }}>ID</TableCell>
								<TableCell sx={{ width: 150 }}>
									From Card
								</TableCell>
								<TableCell sx={{ width: 150 }}>
									To Card
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
							</TableRow>
						</TableHead>
						<TableBody>
							{transactions?.map((transaction, index) => (
								<TableRow key={transaction?.transactionId}>
									<TableCell>
										{(page - 1) * DEFAULT_ITEMS_PER_PAGE +
											index +
											1}
									</TableCell>
									<TableCell>
										{transaction?.transactionId}
									</TableCell>
									<TableCell>
										{transaction?.fromCardNumber}
									</TableCell>
									<TableCell>
										{transaction?.toCardNumber}
									</TableCell>
									<TableCell>{transaction?.amount}</TableCell>
									<TableCell>
										{transaction?.transactionType}
									</TableCell>
									<TableCell>
										{transaction?.transactionStatus}
									</TableCell>
									<TableCell>
										{transaction?.createdAt}
									</TableCell>
									<TableCell>
										{transaction?.updatedAt}
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
								to={`/my-transactions${
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

export default MyTransactions;
