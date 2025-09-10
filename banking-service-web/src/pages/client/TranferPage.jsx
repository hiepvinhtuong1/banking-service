import {
	Card,
	CardContent,
	Typography,
	Button,
	TextField,
	Box,
	Grid,
} from "@mui/material";
import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { toast } from "react-toastify";
import { selectCurrentUser } from "~/redux/user/userSlice";
import { fetchCardByCardNumberAPI, sendOtpAPI } from "~/apis";
import { createPaymentAPI } from "../../apis";
import { useDispatch } from "react-redux";
import { updateBalance } from "~/redux/user/userSlice";

export default function TransferPage() {
	const { cardId } = useParams();
	const navigate = useNavigate();
	const dispatch = useDispatch();
	const currentUser = useSelector(selectCurrentUser);

	// tìm thẻ hiện tại trong danh sách thẻ user
	const card = currentUser?.cards.find((c) => c.cardId === cardId);

	const [receiver, setReceiver] = useState(null);
	const [searchCardId, setSearchCardId] = useState("");
	const [amount, setAmount] = useState("");
	const [otp, setOtp] = useState("");
	const [otpSent, setOtpSent] = useState(false);

	const handleSearch = () => {
		fetchCardByCardNumberAPI(searchCardId).then((res) => {
			setReceiver({
				cardId: res.cardId,
				cardNumber: res.cardNumber,
				name: res.customerName,
				email: res.email,
				phoneNumber: res.phoneNumber,
			});
		});
	};

	// Gửi OTP
	const handleSendOtp = () => {
		if (!amount || Number(amount) <= 0) {
			toast.error("Vui lòng nhập số tiền hợp lệ!");
			return;
		}
		toast.promise(sendOtpAPI(currentUser?.email), {
			pending: "Đang gửi OTP...",
			success: {
				render() {
					setOtpSent(true);
					return "OTP đã được gửi tới email của bạn!";
				},
			},
			error: "Không thể gửi OTP, vui lòng thử lại.",
		});
	};

	// Xác minh OTP + Tạo payment
	const handleVerifyOtp = () => {
		if (!otp) {
			toast.error("Vui lòng nhập OTP!");
			return;
		}
		const request = {
			accountId: currentUser?.accountId,
			fromCardNumber: card.cardNumber,
			toCardNumber: receiver?.cardNumber,
			amount: Number(amount),
			email: currentUser?.email,
			otpCode: otp,
		};

		toast.promise(createPaymentAPI(request), {
			pending: "Đang xử lý giao dịch...",
			success: {
				render({ data }) {
					// Reset dữ liệu sau khi thành công

					// ✅ Cập nhật lại balance trong Redux
					dispatch(
						updateBalance({
							availableBalance:
								currentUser?.balance?.availableBalance -
								data?.amount,
							holdBalance:
								currentUser?.balance?.holdBalance +
								data?.amount,
						})
					);

					setReceiver(null);
					setSearchCardId("");
					setAmount("");
					setOtp("");
					setOtpSent(false);
					return `Chuyển khoản thành công! Mã giao dịch: ${data.transactionId}`;
				},
			},
		});
	};

	if (!card) {
		return <Typography>Không tìm thấy thẻ!</Typography>;
	}

	return (
		<Box sx={{ flexGrow: 1, mt: 3 }}>
			<Typography variant="h4" gutterBottom>
				Chuyển khoản
			</Typography>

			<Grid
				container
				spacing={2}
				sx={{ display: "flex", justifyContent: "space-around" }}
			>
				<Grid item xs={12} md={3} sx={{ display: "flex" }}>
					<Card
						elevation={3}
						sx={{
							flex: 1,
							display: "flex",
							flexDirection: "column",
						}}
					>
						<Box
							sx={{
								bgcolor: "grey.100",
								p: 2,
								borderBottom: 1,
								borderColor: "divider",
							}}
						>
							<Typography
								variant="h6"
								fontWeight="bold"
								align="center"
							>
								Thông tin thẻ của bạn
							</Typography>
						</Box>

						<CardContent sx={{ flexGrow: 1 }}>
							<Box sx={{ mb: 2 }}>
								<Typography variant="body1" sx={{ mb: 1 }}>
									<strong>Số thẻ:</strong> {card.cardNumber}
								</Typography>
								<Typography variant="body1" sx={{ mb: 1 }}>
									<strong>Loại thẻ:</strong> {card.cardType}
								</Typography>
								<Typography variant="body1" sx={{ mb: 1 }}>
									<strong>Số dư:</strong>{" "}
									{new Intl.NumberFormat("vi-VN").format(
										currentUser?.balance
											?.availableBalance || 0
									)}{" "}
									đ
								</Typography>
								<Typography variant="body1">
									<strong>Đang chờ xử lý:</strong>{" "}
									{new Intl.NumberFormat("vi-VN").format(
										currentUser?.balance?.holdBalance || 0
									)}{" "}
									đ
								</Typography>
							</Box>
						</CardContent>

						<Box sx={{ p: 2, pt: 0 }}>
							<Button
								variant="contained"
								color="warning"
								fullWidth
								onClick={() => navigate(-1)}
							>
								Trở lại
							</Button>
						</Box>
					</Card>
				</Grid>

				{/* Card bên phải */}
				<Grid item xs={12} md={9} sx={{ display: "flex" }}>
					<Card
						elevation={3}
						sx={{
							flex: 1,
							display: "flex",
							flexDirection: "column",
						}}
					>
						<Box
							sx={{
								bgcolor: "grey.100",
								p: 2,
								borderBottom: 1,
								borderColor: "divider",
							}}
						>
							<Typography
								variant="h6"
								fontWeight="bold"
								align="center"
							>
								Thông tin người nhận
							</Typography>
						</Box>
						<CardContent sx={{ flexGrow: 1, maxWidth: 800 }}>
							<TextField
								label="Số thẻ"
								variant="outlined"
								fullWidth
								size="small"
								value={searchCardId}
								onChange={(e) =>
									setSearchCardId(e.target.value)
								}
								sx={{ mb: 2 }}
							/>

							<Button
								className="interceptor-loading"
								variant="contained"
								color="primary"
								fullWidth
								onClick={handleSearch}
								sx={{ maxWidth: 100 }}
							>
								Tìm kiếm
							</Button>

							{receiver && (
								<Box sx={{ mt: 3 }}>
									<TextField
										label="Số thẻ người nhận"
										value={receiver?.cardNumber}
										fullWidth
										size="small"
										disabled
										sx={{ mb: 2 }}
									/>
									<TextField
										label="Tên người nhận"
										value={receiver.name}
										fullWidth
										size="small"
										disabled
										sx={{ mb: 2 }}
									/>
									<TextField
										label="Số tiền"
										variant="outlined"
										fullWidth
										size="small"
										value={amount}
										onChange={(e) =>
											setAmount(e.target.value)
										}
										sx={{ mb: 2 }}
									/>

									{/* Nếu chưa gửi OTP thì hiển thị nút Gửi OTP */}
									{!otpSent ? (
										<Button
											className="interceptor-loading"
											variant="contained"
											color="secondary"
											sx={{ maxWidth: 250 }}
											onClick={handleSendOtp}
										>
											Xác nhận chuyển khoản
										</Button>
									) : (
										<Box>
											<TextField
												label="Nhập OTP"
												variant="outlined"
												fullWidth
												size="small"
												value={otp}
												onChange={(e) =>
													setOtp(e.target.value)
												}
												sx={{ mb: 2 }}
											/>
											<Button
												className="interceptor-loading"
												variant="contained"
												color="success"
												sx={{ maxWidth: 250 }}
												onClick={handleVerifyOtp}
											>
												Xác nhận OTP
											</Button>
										</Box>
									)}
								</Box>
							)}
						</CardContent>
					</Card>
				</Grid>
			</Grid>
		</Box>
	);
}
