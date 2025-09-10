import { useForm } from "react-hook-form";
import {
	Box,
	Button,
	TextField,
	Container,
	Typography,
	Paper,
	Breadcrumbs,
} from "@mui/material";
import { Link, Link as RouterLink, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { createNewUserLevelAPI } from "~/apis";
import FieldErrorAlert from "~/components/Form/FieldErrorAlert";
import { FIELD_REQUIRED_MESSAGE } from "~/utils/validators";

function CreateUserLevelForm() {
	const navigate = useNavigate();

	const {
		register,
		handleSubmit,
		formState: { errors },
	} = useForm({
		defaultValues: {
			name: "",
			numberOfCards: "",
			dailyTransactionAmount: "",
		},
	});

	const onSubmit = (data) => {
		createNewUserLevelAPI(data).then(() => {
			toast.success("User level created successfully!");
			navigate("/admin/user-level");
		});
	};

	return (
		<Container disableGutters maxWidth={false}>
			<Box sx={{ paddingX: 2, my: 4 }}>
				{/* Breadcrumb */}
				<Breadcrumbs sx={{ mb: 2 }}>
					<Link
						underline="hover"
						color="inherit"
						component={RouterLink}
						to="/admin/dashboard"
					>
						Dashboard
					</Link>
					<Link
						underline="hover"
						color="inherit"
						component={RouterLink}
						to="/admin/user-level"
					>
						User Level
					</Link>
					<Typography color="text.primary">
						Create User Level
					</Typography>
				</Breadcrumbs>
			</Box>

			<Container maxWidth="sm">
				<Paper sx={{ p: 3, mt: 4 }}>
					<Typography variant="h5" fontWeight="bold" sx={{ mb: 2 }}>
						Create New User Level
					</Typography>

					<form onSubmit={handleSubmit(onSubmit)}>
						<Box
							sx={{
								display: "flex",
								flexDirection: "column",
								gap: 2,
							}}
						>
							{/* Level Name */}
							<TextField
								label="Level Name"
								fullWidth
								error={!!errors["name"]}
								{...register("name", {
									required: FIELD_REQUIRED_MESSAGE,
								})}
							/>
							<FieldErrorAlert
								errors={errors}
								fieldName={"name"}
							/>

							{/* Card Limit */}
							<TextField
								label="Card Limit"
								type="number"
								fullWidth
								error={!!errors["numberOfCards"]}
								{...register("numberOfCards", {
									required: FIELD_REQUIRED_MESSAGE,
									valueAsNumber: true,
								})}
							/>
							<FieldErrorAlert
								errors={errors}
								fieldName={"numberOfCards"}
							/>

							{/* Daily Transfer Limit */}
							<TextField
								label="Daily Transfer Limit"
								type="number"
								fullWidth
								error={!!errors["dailyTransactionAmount"]}
								{...register("dailyTransactionAmount", {
									required: FIELD_REQUIRED_MESSAGE,
									valueAsNumber: true,
								})}
							/>
							<FieldErrorAlert
								errors={errors}
								fieldName={"dailyTransactionAmount"}
							/>

							{/* Buttons */}
							<Box sx={{ display: "flex", gap: 2, mt: 2 }}>
								<Button
									type="submit"
									variant="contained"
									color="primary"
								>
									Create User Level
								</Button>
								<Button
									variant="outlined"
									color="secondary"
									onClick={() => navigate("/user-level")}
								>
									Cancel
								</Button>
							</Box>
						</Box>
					</form>
				</Paper>
			</Container>
		</Container>
	);
}

export default CreateUserLevelForm;
