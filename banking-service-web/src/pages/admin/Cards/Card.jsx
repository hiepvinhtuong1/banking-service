import { useEffect, useState } from "react";
import {
	Container,
	Box,
	Typography,
	Stack,
	Button,
	Breadcrumbs,
	Paper,
	Grid,
	TextField,
} from "@mui/material";
import { Link, useParams, useNavigate } from "react-router-dom";
import {
	fetchCardByIdAPI,
	depositAPI,
	withdrawAPI,
	fetchBalanceByAccountIdAPI,
} from "~/apis";

function CardDetail() {
	const { cardId } = useParams();
	console.log("🚀 ~ Card ~ id:", cardId);
	const [card, setCard] = useState(null);
	const [balance, setBalance] = useState(null);
	const [depositAmount, setDepositAmount] = useState("");
	const [withdrawAmount, setWithdrawAmount] = useState("");
	const navigate = useNavigate();

	useEffect(() => {
		const loadData = async () => {
			// cardRes là object trả về từ API
			const cardRes = await fetchCardByIdAPI(cardId);
			setCard(cardRes);

			if (cardRes?.accountId) {
				const balanceRes = await fetchBalanceByAccountIdAPI(
					cardRes.accountId
				);
				setBalance(balanceRes);
			}
		};

		loadData();
	}, [cardId]);

	const handleDeposit = async () => {
		if (!depositAmount) return;
		await depositAPI(cardId, depositAmount);
		// Refresh balance sau khi nạp
		fetchCardByIdAPI(cardId).then((res) => setBalance(res?.balance));
		setDepositAmount("");
	};

	const handleWithdraw = async () => {
		if (!withdrawAmount) return;
		await withdrawAPI(cardId, withdrawAmount);
		// Refresh balance sau khi rút
		fetchCardByIdAPI(cardId).then((res) => setBalance(res?.balance));
		setWithdrawAmount("");
	};

	return (
		<Grid
			container
			spacing={4}
			justifyContent="space-around"
			alignItems="stretch" // quan trọng: cho các item cao bằng nhau
		>
			{/* Card Info */}
			<Grid item xs={12} md={5} display="flex">
				<Paper sx={{ p: 3, width: "100%", height: "100%" }}>
					<Typography variant="h6" fontWeight="bold" sx={{ mb: 2 }}>
						Card Information
					</Typography>
					<Stack spacing={1} flexGrow={1}>
						<Typography>Card ID: {card?.cardId}</Typography>
						<Typography>Card Number: {card?.cardNumber}</Typography>
						<Typography>Card Type: {card?.cardType}</Typography>
						<Typography>Expiry Date: {card?.expiryDate}</Typography>
						<Typography>Status: {card?.status}</Typography>
						<Typography>Account ID: {card?.accountId}</Typography>
						<Typography>
							Customer name: {card?.customerName}
						</Typography>
						<Typography>Account Email: {card?.email}</Typography>
					</Stack>

					<Button
						variant="contained"
						color="primary"
						sx={{ mt: 3 }}
						onClick={() => navigate("/account")}
					>
						Back to User
					</Button>
				</Paper>
			</Grid>

			{/* Balance Info */}
			<Grid item xs={12} md={5} display="flex">
				<Paper
					sx={{
						p: 3,
						width: "100%",
						height: "100%",
						display: "flex",
						flexDirection: "column",
					}}
				>
					<Typography variant="h6" fontWeight="bold" sx={{ mb: 2 }}>
						Balance Information
					</Typography>
					<Stack spacing={1} flexGrow={1}>
						<Typography>
							Balance ID: {balance?.balanceId}
						</Typography>
						<Typography>
							Available Balance: {balance?.availableBalance}
						</Typography>
						<Typography>
							Hold Balance: {balance?.holdBalance}
						</Typography>
						<Typography>
							Last Updated: {balance?.updatedAt}
						</Typography>
					</Stack>

					{/* Deposit */}
					<Box sx={{ mt: 3 }}>
						<TextField
							label="Deposit Amount"
							type="number"
							size="small"
							value={depositAmount}
							onChange={(e) => setDepositAmount(e.target.value)}
							fullWidth
						/>
						<Button
							variant="contained"
							color="success"
							sx={{ mt: 2, width: "100%" }}
							onClick={handleDeposit}
						>
							Deposit
						</Button>
					</Box>

					{/* Withdraw */}
					<Box sx={{ mt: 3 }}>
						<TextField
							label="Withdraw Amount"
							type="number"
							size="small"
							value={withdrawAmount}
							onChange={(e) => setWithdrawAmount(e.target.value)}
							fullWidth
						/>
						<Button
							variant="contained"
							color="error"
							sx={{ mt: 2, width: "100%" }}
							onClick={handleWithdraw}
						>
							Withdraw
						</Button>
					</Box>
				</Paper>
			</Grid>
		</Grid>
	);
}

export default CardDetail;
