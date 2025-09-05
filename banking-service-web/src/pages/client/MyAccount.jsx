import { Container, Typography } from "@mui/material";
import AccountInfo from "../../components/AccountInfo/AccountInfo";
import CardList from "../../components/AccountInfo/CardList";

export default function MyAccount() {
	return (
		<Container maxWidth="lg" sx={{ mt: 4 }}>
			<Typography variant="h4" gutterBottom>
				Thông tin Tài khoản và Chuyển khoản
			</Typography>
			<AccountInfo />
			<CardList />
		</Container>
	);
}
