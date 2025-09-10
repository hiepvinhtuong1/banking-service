import { useEffect, useState } from "react";
import {
	Box,
	Button,
	TextField,
	MenuItem,
	Container,
	Typography,
	Paper,
	Breadcrumbs,
} from "@mui/material";
import { useForm } from "react-hook-form";
import { toast } from "react-toastify";
import { Link, Link as RouterLink, useNavigate } from "react-router-dom";
import FieldErrorAlert from "~/components/Form/FieldErrorAlert";
import {
	createNewCardAPI,
	fetchCardTypesAPI,
	fetchCardStatusAPI,
} from "~/apis";
import { FIELD_REQUIRED_MESSAGE } from "~/utils/validators";
import { useDispatch, useSelector } from "react-redux";
import { selectCurrentUser, addNewCard } from "~/redux/user/userSlice";

function CreateUserCardForm() {
	const dispatch = useDispatch();
	const navigate = useNavigate();
	const currentUser = useSelector(selectCurrentUser);
	const accountId = currentUser?.accountId; // 👉 lấy trực tiếp từ redux
	const {
		register,
		handleSubmit,
		formState: { errors },
	} = useForm({
		defaultValues: {
			expiryDate: "",
			cardType: "",
			status: "",
		},
	});

	const [cardTypes, setCardTypes] = useState([]);
	const [statuses, setStatuses] = useState([]);

	// load dữ liệu card type & status từ API
	useEffect(() => {
		fetchCardTypesAPI().then(setCardTypes);
		fetchCardStatusAPI().then(setStatuses);
	}, []);

	const onSubmit = (data) => {
		createNewCardAPI({
			...data,
			accountId,
		}).then((res) => {
			console.log("🚀 ~ onSubmit ~ res:", res);
			dispatch(addNewCard(res));
			toast.success("Card created successfully!");
			navigate(`/app/my-account`);
		});
	};

	return (
		<Container disableGutters maxWidth={false}>
			<Box sx={{ paddingX: 2, my: 4 }}></Box>
			<Container maxWidth="sm">
				<Paper sx={{ p: 3, mt: 4 }}>
					<Typography variant="h5" fontWeight="bold" sx={{ mb: 2 }}>
						Create New Card
					</Typography>

					<form onSubmit={handleSubmit(onSubmit)}>
						<Box
							sx={{
								display: "flex",
								flexDirection: "column",
								gap: 2,
							}}
						>
							{/* Expiry Date */}
							<TextField
								label="Expiry Date"
								type="date"
								InputLabelProps={{ shrink: true }}
								fullWidth
								error={!!errors["expiryDate"]}
								{...register("expiryDate", {
									required: FIELD_REQUIRED_MESSAGE,
								})}
							/>
							<FieldErrorAlert
								errors={errors}
								fieldName="expiryDate"
							/>

							{/* Card Type */}
							<TextField
								select
								label="Card Type"
								fullWidth
								error={!!errors["cardType"]}
								{...register("cardType", {
									required: FIELD_REQUIRED_MESSAGE,
								})}
							>
								{cardTypes.map((ct, index) => (
									<MenuItem key={index} value={ct}>
										{ct}
									</MenuItem>
								))}
							</TextField>
							<FieldErrorAlert
								errors={errors}
								fieldName="cardType"
							/>

							{/* Status */}
							<TextField
								select
								label="Status"
								fullWidth
								error={!!errors["status"]}
								{...register("status", {
									required: FIELD_REQUIRED_MESSAGE,
								})}
							>
								{statuses.map((s, index) => (
									<MenuItem key={index} value={s}>
										{s}
									</MenuItem>
								))}
							</TextField>
							<FieldErrorAlert
								errors={errors}
								fieldName="status"
							/>

							<Button
								type="submit"
								variant="contained"
								color="primary"
							>
								Create Card
							</Button>
						</Box>
					</form>
				</Paper>
			</Container>
		</Container>
	);
}

export default CreateUserCardForm;
