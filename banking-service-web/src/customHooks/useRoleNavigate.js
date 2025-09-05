import { useNavigate } from "react-router-dom";
import { useSelector } from "react-redux";
import { selectCurrentUser } from "~/redux/user/userSlice";

export function useRoleNavigate() {
    const navigate = useNavigate();
    const currentUser = useSelector(selectCurrentUser);

    return (path = "", options) => {
        let prefix = "";
        if (currentUser?.roles?.includes("ADMIN")) prefix = "/admin";
        if (currentUser?.roles?.includes("USER")) prefix = "/app";

        // Xử lý: nếu path = "/" → chỉ dùng prefix thôi
        const finalPath = path === "/" ? prefix : prefix + path;

        navigate(finalPath, options);
    };
}
