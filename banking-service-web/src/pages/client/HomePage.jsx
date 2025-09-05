import { Box, Button, Container, Grid, Paper, Typography } from "@mui/material";
import { useSelector } from "react-redux";
import { selectCurrentUser } from "~/redux/user/userSlice";

function HomePage() {
	const currentUser = useSelector(selectCurrentUser);

	return (
		<>
			{/* Hero Section */}
			<Box
				sx={{
					height: "300px",
					backgroundColor: "#ccc",
					display: "flex",
					flexDirection: "column",
					alignItems: "center",
					justifyContent: "center",
					textAlign: "center",
				}}
			>
				<Typography variant="h4" fontWeight="bold" gutterBottom>
					Chào mừng {currentUser?.customerName}
				</Typography>
				<Typography variant="subtitle1" gutterBottom>
					Ngân hàng số - Giải pháp tài chính hiện đại và an toàn
				</Typography>
				<Button variant="contained" color="primary">
					Tìm hiểu thêm
				</Button>
			</Box>

			{/* Features Section */}
			<Container maxWidth="lg" sx={{ mt: 4 }}>
				<Grid
					container
					spacing={4}
					justifyContent="center"
					wrap="nowrap" // không cho xuống hàng
				>
					<Grid item sx={{ width: 300 }}>
						<Paper
							sx={{ p: 3, textAlign: "center", height: "100%" }}
						>
							<Typography variant="h5">💲</Typography>
							<Typography variant="h6" sx={{ mt: 1 }}>
								Giao dịch An Toàn
							</Typography>
							<Typography variant="body2">
								Đảm bảo mọi giao dịch đều bảo mật và nhanh
								chóng.
							</Typography>
						</Paper>
					</Grid>

					<Grid item sx={{ width: 300 }}>
						<Paper
							sx={{ p: 3, textAlign: "center", height: "100%" }}
						>
							<Typography variant="h5">📊</Typography>
							<Typography variant="h6" sx={{ mt: 1 }}>
								Tư Vấn Tài Chính
							</Typography>
							<Typography variant="body2">
								Giúp bạn đưa ra quyết định tài chính thông minh.
							</Typography>
						</Paper>
					</Grid>

					<Grid item sx={{ width: 300 }}>
						<Paper
							sx={{ p: 3, textAlign: "center", height: "100%" }}
						>
							<Typography variant="h5">🎧</Typography>
							<Typography variant="h6" sx={{ mt: 1 }}>
								Hỗ Trợ 24/7
							</Typography>
							<Typography variant="body2">
								Đội ngũ hỗ trợ khách hàng luôn sẵn sàng phục vụ
								bạn.
							</Typography>
						</Paper>
					</Grid>
				</Grid>
			</Container>
		</>
	);
}

export default HomePage;
