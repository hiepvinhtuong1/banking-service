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
	PaginationItem,
} from "@mui/material";
import { fetchCardsAPI } from "~/apis"; // <-- API call cho card
import { Link, useLocation, useNavigate } from "react-router-dom";
import { DEFAULT_ITEMS_PER_PAGE, DEFAULT_PAGE } from "~/utils/constant";

function Cards() {
	const [cards, setCards] = useState([]);
	const [totalCards, setTotalCards] = useState(0);
	const navigate = useNavigate();
	const location = useLocation();

	// Lấy page từ query
	const query = new URLSearchParams(location.search);
	const page = parseInt(query.get("page") || "1", 10);

	const handleSearchNumber = (event) => {
		if (event.key === "Enter" && !event.shiftKey) {
			event.preventDefault();

			query.set("numberCard", event.target?.value);
			query.set("page", DEFAULT_PAGE);
			query.set("size", DEFAULT_ITEMS_PER_PAGE);

			navigate(`/card?${query.toString()}`);
		}
	};

	useEffect(() => {
		fetchCardsAPI(location.search).then((res) => {
			setCards(res?.content || []);
			setTotalCards(res?.totalElements || 0);
		});
	}, [location.search]);

	return (
		<Container disableGutters maxWidth={false}>
			<Box sx={{ paddingX: 2, my: 4 }}>
				{/* Breadcrumb */}
				<Breadcrumbs sx={{ mb: 2 }}>
					<Link
						underline="hover"
						color="inherit"
						href="/admin/dashboard"
					>
						Dashboard
					</Link>
					<Typography color="text.primary">Card</Typography>
				</Breadcrumbs>

				{/* Title */}
				<Stack
					direction="row"
					alignItems="center"
					justifyContent="space-between"
					sx={{ mb: 2 }}
				>
					<Typography variant="h5" fontWeight="bold">
						Table Card
					</Typography>
				</Stack>

				{/* Search */}
				<Box
					sx={{ mb: 2, display: "flex", justifyContent: "flex-end" }}
				>
					<TextField
						placeholder="Search by numberCard"
						size="small"
						sx={{ width: 300 }}
						onKeyDown={handleSearchNumber}
					/>
				</Box>

				{/* Table */}
				<TableContainer component={Paper}>
					<Table>
						<TableHead>
							<TableRow>
								<TableCell>ID</TableCell>
								<TableCell>Customer Name</TableCell>
								<TableCell>Card number</TableCell>
								<TableCell>Card type</TableCell>
								<TableCell>Status</TableCell>
								<TableCell align="center">Actions</TableCell>
							</TableRow>
						</TableHead>
						<TableBody>
							{cards?.map((card, index) => (
								<TableRow key={card?.cardId}>
									<TableCell>
										{(page - 1) * DEFAULT_ITEMS_PER_PAGE +
											index +
											1}
									</TableCell>
									<TableCell>{card?.customerName}</TableCell>
									<TableCell>{card?.cardNumber}</TableCell>
									<TableCell>{card?.cardType}</TableCell>
									<TableCell>{card?.status}</TableCell>
									<TableCell align="center">
										<Stack
											direction="row"
											spacing={1}
											justifyContent="center"
										>
											<Button
												variant="contained"
												color="primary"
												size="small"
												onClick={() =>
													navigate(
														`/admin/card/${card?.cardId}`
													)
												}
											>
												View
											</Button>
											<Button
												variant="contained"
												color="error"
												size="small"
											>
												Delete
											</Button>
											<Button
												variant="contained"
												color="error"
												size="small"
											>
												{card?.status === "ACTIVE"
													? "Update INACTIVE"
													: "Update ACTIVE"}
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
						count={Math.ceil(totalCards / DEFAULT_ITEMS_PER_PAGE)}
						page={page}
						color="primary"
						renderItem={(item) => (
							<PaginationItem
								component={Link}
								to={`/card${
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

export default Cards;
