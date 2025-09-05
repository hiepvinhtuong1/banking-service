import { Card, CardContent, Typography, Button, Box } from "@mui/material";
import { useSelector } from "react-redux";
import { selectCurrentUser } from "~/redux/user/userSlice";
import { useRoleNavigate } from "../../customHooks/useRoleNavigate";
export default function CardList() {
	const currentUser = useSelector(selectCurrentUser);
	const navigate = useRoleNavigate();
	return (
		<Card sx={{ width: "100%" }}>
			<CardContent>
				<Typography variant="h5" gutterBottom>
					Danh sách thẻ của bạn
				</Typography>
				<Button variant="contained" color="warning" sx={{ mb: 2 }}>
					Create Card
				</Button>

				{/* Flex layout cố định width card */}
				<Box
					sx={{
						display: "flex",
						flexWrap: "wrap",
						gap: 2,
					}}
				>
					{currentUser?.cards.map((card) => (
						<Box
							key={card.cardId}
							sx={{
								p: 2,
								border: "1px solid #ddd",
								borderRadius: 2,
								bgcolor: "#fafafa",
								width: 250, // 👈 cố định width
							}}
						>
							<Typography>
								<b>Số thẻ:</b> {card?.cardNumber}
							</Typography>
							<Typography>
								<b>Trạng thái:</b> {card?.status}
							</Typography>
							<Typography>
								<b>Loại thẻ:</b> {card.cardType}
							</Typography>
							<Typography>
								<b>Ngày hết hạn:</b> {card.expiryDate}
							</Typography>
							<Button
								variant="outlined"
								size="small"
								sx={{ mt: 1 }}
								fullWidth
								onClick={() =>
									navigate(`/transfer/${card.cardId}`)
								}
							>
								Chuyển khoản
							</Button>
						</Box>
					))}
				</Box>
			</CardContent>
		</Card>
	);
}
