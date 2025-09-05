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
import { fetchRoleAPI, fetchUserLevelAPI } from "~/apis";
import { toast } from "react-toastify";

import {
	PASSWORD_RULE,
	PASSWORD_RULE_MESSAGE,
	EMAIL_RULE,
	EMAIL_RULE_MESSAGE,
	FIELD_REQUIRED_MESSAGE,
	PHONE_RULE,
	PHONE_RULE_MESSAGE,
	PASSWORD_CONFIRMATION_MESSAGE,
} from "~/utils/validators";
import FieldErrorAlert from "~/components/Form/FieldErrorAlert";
import { Link, Link as RouterLink, useNavigate } from "react-router-dom";
import { createNewAccountAPI } from "../../../apis";
function CreateAccountForm() {
	const navigate = useNavigate();
	const {
		register,
		handleSubmit,
		watch,
		formState: { errors },
	} = useForm({
		defaultValues: {
			email: "",
			phoneNumber: "",
			password: "",
			password_confirmation: "",
			role: "", // 👈 thêm mặc định là rỗng
			level: "", // 👈 thêm mặc định là rỗng
		},
	});

	const [roles, setRoles] = useState([]);
	const [levels, setLevels] = useState([]);

	// Tải dữ liệu từ API khi component mount
	useEffect(() => {
		fetchRoleAPI().then(setRoles);
		fetchUserLevelAPI().then(setLevels);
	}, []);

	const onSubmit = (data) => {
		createNewAccountAPI(data).then(() => {
			toast.success("Account created successfully!");
			navigate("/account");
		});
	};

	return (
		<Container disableGutters maxWidth={false}>
			<Box sx={{ paddingX: 2, my: 4 }}>
				<Breadcrumbs sx={{ mb: 2 }}>
					<Link
						underline="hover"
						color="inherit"
						component={RouterLink}
						to="/dashboard"
					>
						Dashboard
					</Link>
					<Link
						underline="hover"
						color="inherit"
						component={RouterLink}
						to="/account"
					>
						Account
					</Link>
					<Typography color="text.primary">Create Account</Typography>
				</Breadcrumbs>
			</Box>
			<Container maxWidth="sm">
				<Paper sx={{ p: 3, mt: 4 }}>
					<Typography variant="h5" fontWeight="bold" sx={{ mb: 2 }}>
						Create Account
					</Typography>

					<form onSubmit={handleSubmit(onSubmit)}>
						<Box
							sx={{
								display: "flex",
								flexDirection: "column",
								gap: 2,
							}}
						>
							{/* Email */}
							<TextField
								label="Email"
								fullWidth
								error={!!errors["email"]}
								{...register("email", {
									required: FIELD_REQUIRED_MESSAGE,
									pattern: {
										value: EMAIL_RULE,
										message: EMAIL_RULE_MESSAGE,
									},
								})}
							/>
							<FieldErrorAlert
								errors={errors}
								fieldName={"email"}
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
								fieldName={"phoneNumber"}
							/>

							{/* Password */}
							<TextField
								label="Password"
								type="password"
								fullWidth
								error={!!errors["password"]}
								{...register("password", {
									required: FIELD_REQUIRED_MESSAGE,
									pattern: {
										value: PASSWORD_RULE,
										message: PASSWORD_RULE_MESSAGE,
									},
								})}
							/>
							<FieldErrorAlert
								errors={errors}
								fieldName={"password"}
							/>

							{/* Confirm Password */}
							<TextField
								label="Confirm Password"
								type="password"
								fullWidth
								{...register("password_confirmation", {
									validate: (value) => {
										if (value === watch("password")) {
											return true;
										}

										return PASSWORD_CONFIRMATION_MESSAGE;
									},
								})}
							/>
							<FieldErrorAlert
								errors={errors}
								fieldName={"password_confirmation"}
							/>

							{/* Role */}
							<TextField
								select
								label="Role"
								fullWidth
								error={!!errors["role"]}
								{...register("role", {
									required: FIELD_REQUIRED_MESSAGE,
								})}
							>
								{roles.map((r) => (
									<MenuItem key={r.id} value={r.name}>
										{r.name}
									</MenuItem>
								))}
							</TextField>
							<FieldErrorAlert
								errors={errors}
								fieldName={"role"}
							/>

							{/* User Level */}
							<TextField
								select
								label="User Level"
								fullWidth
								error={!!errors["level"]}
								{...register("level", {
									required: FIELD_REQUIRED_MESSAGE,
								})}
							>
								{levels.map((l) => (
									<MenuItem key={l.id} value={l?.id}>
										{l.name}
									</MenuItem>
								))}
							</TextField>
							<FieldErrorAlert
								errors={errors}
								fieldName={"level"}
							/>

							<Button
								type="submit"
								variant="contained"
								color="primary"
							>
								Create Account
							</Button>
						</Box>
					</form>
				</Paper>
			</Container>
		</Container>
	);
}

export default CreateAccountForm;
