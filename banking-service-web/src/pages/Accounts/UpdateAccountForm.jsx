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
import { Controller, useForm } from "react-hook-form";
import { useParams, useNavigate, Link as RouterLink } from "react-router-dom";
import {
	fetchRoleAPI,
	fetchUserLevelAPI,
	getDetailAccountAPI,
	updateAccountAPI,
} from "~/apis";
import { toast } from "react-toastify";
import {
	FIELD_REQUIRED_MESSAGE,
	PHONE_RULE,
	PHONE_RULE_MESSAGE,
} from "~/utils/validators";
import FieldErrorAlert from "~/components/Form/FieldErrorAlert";

function UpdateAccountForm() {
	const { accountId } = useParams();
	const navigate = useNavigate();

	const [roles, setRoles] = useState([]);
	const [levels, setLevels] = useState([]);
	const [loading, setLoading] = useState(true);

	const {
		register,
		handleSubmit,
		reset,
		control,
		formState: { errors },
	} = useForm();

	useEffect(() => {
		// load role & level song song
		fetchRoleAPI().then(setRoles);
		fetchUserLevelAPI().then(setLevels);

		// load account by id
		getDetailAccountAPI(accountId).then((account) => {
			console.log("🚀 ~ UpdateAccountForm ~ account:", account);
			reset({
				email: account.email,
				customerName: account.customerName || "", // 👈 thêm field
				phoneNumber: account.phoneNumber,
				role: account.roles?.[0]?.name || "",
				level: account.userLevel?.id || "",
			});
			setLoading(false);
		});
	}, [accountId, reset]);

	const onSubmit = (data) => {
		console.log("🚀 ~ onSubmit ~ data:", data);
		updateAccountAPI(accountId, data).then(() => {
			toast.success("Account updated successfully!");
			navigate("/account");
		});
	};

	if (loading) return <Typography sx={{ m: 4 }}>Loading...</Typography>;

	return (
		<Container disableGutters maxWidth={false}>
			<Box sx={{ paddingX: 2, my: 4 }}>
				<Breadcrumbs sx={{ mb: 2 }}>
					<RouterLink to="/dashboard">Dashboard</RouterLink>
					<RouterLink to="/account">Account</RouterLink>
					<Typography color="text.primary">Update Account</Typography>
				</Breadcrumbs>
			</Box>
			<Container maxWidth="sm">
				<Paper sx={{ p: 3, mt: 4 }}>
					<Typography variant="h5" fontWeight="bold" sx={{ mb: 2 }}>
						Update Account
					</Typography>

					<form onSubmit={handleSubmit(onSubmit)}>
						<Box
							sx={{
								display: "flex",
								flexDirection: "column",
								gap: 2,
							}}
						>
							{/* Email (không cho chỉnh sửa) */}
							<TextField
								label="Email"
								fullWidth
								disabled
								{...register("email")}
							/>
							{/* Customer Name (chỉ hiển thị, không edit) */}
							<TextField
								label="Customer Name"
								fullWidth
								error={!!errors["customerName"]}
								{...register("customerName", {
									required: FIELD_REQUIRED_MESSAGE,
								})}
							/>
							<FieldErrorAlert
								errors={errors}
								fieldName="customerName"
							/>
							{/* Phone */}
							<TextField
								label="Phone Number"
								fullWidth
								error={!!errors["phoneNumber"]}
								{...register("phoneNumber", {
									required: FIELD_REQUIRED_MESSAGE,
									pattern: {
										value: PHONE_RULE,
										message: PHONE_RULE_MESSAGE,
									},
								})}
							/>
							<FieldErrorAlert
								errors={errors}
								fieldName="phoneNumber"
							/>
							{/* Role */}
							<Controller
								name="role"
								control={control}
								rules={{ required: FIELD_REQUIRED_MESSAGE }}
								render={({ field }) => (
									<TextField
										select
										label="Role"
										fullWidth
										error={!!errors["role"]}
										value={field.value || ""}
										onChange={field.onChange}
									>
										{roles.map((r) => (
											<MenuItem key={r.id} value={r.name}>
												{r.name}
											</MenuItem>
										))}
									</TextField>
								)}
							/>

							<FieldErrorAlert errors={errors} fieldName="role" />

							<Controller
								name="level"
								control={control}
								rules={{ required: FIELD_REQUIRED_MESSAGE }}
								render={({ field }) => (
									<TextField
										select
										label="User Level"
										fullWidth
										error={!!errors["level"]}
										value={field.value || ""}
										onChange={field.onChange}
									>
										{levels.map((l) => (
											<MenuItem key={l.id} value={l.id}>
												{l.name}
											</MenuItem>
										))}
									</TextField>
								)}
							/>
							<FieldErrorAlert
								errors={errors}
								fieldName="level"
							/>
							<Button
								type="submit"
								variant="contained"
								color="primary"
							>
								Update Account
							</Button>
						</Box>
					</form>
				</Paper>
			</Container>
		</Container>
	);
}

export default UpdateAccountForm;
