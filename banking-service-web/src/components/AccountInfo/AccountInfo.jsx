import { Card, CardContent, Typography, Box } from "@mui/material";
import { useSelector } from "react-redux";
import { selectCurrentUser } from "~/redux/user/userSlice";
export default function AccountInfo() {
	const currentUser = useSelector(selectCurrentUser);

	return (
		<Card sx={{ width: "100%", mb: 3 }}>
			<CardContent>
				<Typography variant="h5" gutterBottom>
					Thông tin Tài khoản
				</Typography>
				<Box>
					<Typography>
						<b>Họ và tên:</b> {currentUser?.customerName}
					</Typography>
					<Typography>
						<b>Email:</b> {currentUser?.email}
					</Typography>

					<Typography>
						<b>Số điện thoại:</b> {currentUser?.phoneNumber}
					</Typography>
					<Typography>
						<b>Vai trò:</b> {currentUser?.roles?.[0]}
					</Typography>
					<Typography>
						<b>Cấp độ:</b> {currentUser?.userLevel?.name}
					</Typography>
					<Typography>
						<b>Số dư :</b> {currentUser?.balance?.availableBalance}
					</Typography>
					<Typography>
						<b>Đang chờ xử lí:</b>
						{currentUser?.balance?.holdBalance}
					</Typography>
				</Box>
			</CardContent>
		</Card>
	);
}
