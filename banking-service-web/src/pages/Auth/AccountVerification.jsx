import React, { useEffect, useState } from "react";
import { Navigate, useSearchParams } from "react-router-dom";
import PageLoadingSpinner from "~/components/Loading/PageLoadingSpinner";
import { verifyAccountAPI } from "~/apis";
function AccountVerification() {
	//Lấy giá trị email  và token từ URL
	let [searchParams] = useSearchParams();
	// const email = searchParams.get("email");
	// const token = searchParams.get("token");
	const { email, code } = Object.fromEntries([...searchParams]);

	// Tạo một biến state để biết được đã verify tài khoản thành công hay chưa
	const [verified, setVerified] = useState(false);

	// GỌi API để verify tài khoản
	useEffect(() => {
		if (email && code) {
			verifyAccountAPI({ email, code }).then(() => setVerified(true));
		}
	}, [email, code]);

	// Nếu url có vấn đề, không tồn tại 1 trong 2 email hoặc token thì đá ra trang 404
	if (!email || !code) {
		return <Navigate to="/404" />;
	}
	// Nếu chưa verify xong thì hiện loading
	if (!verified) {
		return <PageLoadingSpinner caption="Verifying your account" />;
	}
	// Cuối cùng nếu không gặp vấn đề gì và verify thành công thì điều hướng về trang login cùng với giá trị verifiedEmail
	return <Navigate to={`/login?verifiedEmail=${email}`} />;
}

export default AccountVerification;
