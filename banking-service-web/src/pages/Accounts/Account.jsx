import { useEffect } from "react";
import {
	Container,
	Box,
	Typography,
	Stack,
	Button,
	Card,
	CardContent,
	CardActions,
	Breadcrumbs,
	Table,
	TableBody,
	TableCell,
	TableContainer,
	TableRow,
	Paper,
	Grid,
} from "@mui/material";
import { Link, useParams, useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import {
	selectCurrentActiveAccount,
	fetchAccountDetailsAPI,
} from "~/redux/account/accountSlice";
import PageLoadingSpinner from "~/components/Loading/PageLoadingSpinner";

function Account() {
	const dispatch = useDispatch();
	const account = useSelector(selectCurrentActiveAccount);
	const { accountId } = useParams();
	const navigate = useNavigate();

	useEffect(() => {
		dispatch(fetchAccountDetailsAPI(accountId));
	}, [dispatch, accountId]);

	if (!account) {
		return <PageLoadingSpinner caption="Loading Account ..." />;
	}

	// cards lấy trực tiếp từ account trả về (nếu API đã trả trong account)
	const cards = account?.cards || [];

	return (
		<Container disableGutters maxWidth={false}>
			<Box sx={{ paddingX: 2, my: 4 }}>
				{/* Breadcrumb */}
				<Breadcrumbs sx={{ mb: 2 }}>
					<Link underline="hover" color="inherit" to="/dashboard">
						Dashboard
					</Link>
					<Link underline="hover" color="inherit" to="/account">
						Account
					</Link>
					<Typography color="text.primary">Detail</Typography>
				</Breadcrumbs>

				<Typography variant="h5" fontWeight="bold" sx={{ mb: 3 }}>
					Detail Account
				</Typography>

				{/* Chi tiết account full width */}
				<Box sx={{ mb: 4 }}>
					<TableContainer component={Paper}>
						<Table size="small">
							<TableBody>
								<TableRow>
									<TableCell>ID:</TableCell>
									<TableCell>{account?.accountId}</TableCell>
								</TableRow>
								<TableRow>
									<TableCell>Email:</TableCell>
									<TableCell>{account?.email}</TableCell>
								</TableRow>
								<TableRow>
									<TableCell>Customer Name:</TableCell>
									<TableCell>
										{account?.customerName}
									</TableCell>
								</TableRow>
								<TableRow>
									<TableCell>Phone:</TableCell>
									<TableCell>
										{account?.phoneNumber}
									</TableCell>
								</TableRow>
								<TableRow>
									<TableCell>Role:</TableCell>
									<TableCell>
										{account?.roles?.[0]?.name}
									</TableCell>
								</TableRow>
								<TableRow>
									<TableCell>User Level:</TableCell>
									<TableCell>
										{account?.userLevel?.name}
									</TableCell>
								</TableRow>
							</TableBody>
						</Table>
					</TableContainer>
				</Box>
				{cards?.length === 0 && (
					<Typography
						variant="span"
						sx={{ fontWeight: "bold", mb: 3 }}
					>
						No result found!
					</Typography>
				)}
				{/* User cards */}

				{cards?.length > 0 && (
					<Box>
						<Stack
							direction="row"
							justifyContent="space-between"
							alignItems="center"
							sx={{ mb: 2 }}
						>
							<Typography variant="h6">User Cards</Typography>
							<Button
								variant="contained"
								color="primary"
								onClick={() => navigate("/card/new")}
							>
								Create Card
							</Button>
						</Stack>

						<Grid container spacing={4}>
							{cards.map((card) => (
								<Grid xs={2} sm={3} md={4} key={card}>
									<Card
										key={card?.cardId}
										variant="outlined"
										sx={{ width: 300 }}
									>
										<CardContent>
											<Typography fontWeight="bold">
												Card Number: {card?.cardNumber}
											</Typography>
											<Typography variant="body2">
												Expiry Date: {card?.expiryDate}
											</Typography>
											<Typography variant="body2">
												Type: {card?.cardType}
											</Typography>
											<Typography variant="body2">
												Status: {card?.status}
											</Typography>
										</CardContent>
										<CardActions>
											<Button
												variant="contained"
												size="small"
												onClick={() =>
													navigate(
														`/card/${card.cardNumber}`
													)
												}
											>
												View Details
											</Button>
										</CardActions>
									</Card>
								</Grid>
							))}
						</Grid>
					</Box>
				)}

				<Button
					variant="contained"
					color="primary"
					sx={{ mt: 4 }}
					onClick={() => navigate("/account")}
				>
					Back
				</Button>
			</Box>
		</Container>
	);
}

export default Account;
