import { Grid, Card, CardContent, Typography, Button } from "@mui/material";

const cards = [
	{ title: "Primary Card", color: "primary" },
	{ title: "Warning Card", color: "warning" },
	{ title: "Success Card", color: "success" },
	{ title: "Danger Card", color: "error" },
];

export default function Dashboard() {
	return (
		<Grid container spacing={2}>
			{cards.map((c) => (
				<Grid item xs={12} sm={6} md={3} key={c.title}>
					<Card sx={{ bgcolor: `${c.color}.main`, color: "white" }}>
						<CardContent>
							<Typography variant="h6">{c.title}</Typography>
							<Button variant="text" sx={{ color: "white" }}>
								View Details
							</Button>
						</CardContent>
					</Card>
				</Grid>
			))}
		</Grid>
	);
}
