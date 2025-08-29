package com.tuanhiep.banking_service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper; // Thư viện Jackson để chuyển đối tượng Java thành JSON
import com.tuanhiep.banking_service.dto.response.APIResponse; // DTO định dạng response của bạn (code, message, data)
import com.tuanhiep.banking_service.exception.AppException;
import com.tuanhiep.banking_service.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus; // Enum chứa các mã trạng thái HTTP (như 401, 400)
import org.springframework.security.core.AuthenticationException; // Lớp cơ bản của Spring Security cho các lỗi xác thực
import org.springframework.security.web.AuthenticationEntryPoint; // Interface của Spring Security để xử lý lỗi xác thực
import org.springframework.stereotype.Component; // Đánh dấu class là Spring Bean để được quản lý bởi Spring


import java.io.IOException; // Ngoại lệ liên quan đến I/O (như ghi response)

/**
 * JwtAuthenticationEntryPoint là một Spring Bean thực hiện interface AuthenticationEntryPoint.
 * Mục đích: Xử lý các lỗi xác thực (authentication errors) khi client gửi request với token không hợp lệ
 * hoặc hết hạn trong Spring Security OAuth2 Resource Server.
 * Khi Spring Security phát hiện lỗi xác thực (như token hết hạn hoặc không hợp lệ),
 * class này được gọi để tạo response định dạng JSON theo cấu trúc APIResponse.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * ObjectMapper là một instance của thư viện Jackson, dùng để chuyển đổi
     * đối tượng Java (như APIResponse) thành chuỗi JSON để gửi về client.
     * Được khởi tạo một lần và tái sử dụng để tránh tạo mới nhiều lần.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Phương thức commence được gọi khi có lỗi xác thực (AuthenticationException),
     * ví dụ: token hết hạn (TOKEN_EXPIRED_EXCEPTION) hoặc token không hợp lệ (UNAUTHENTICATED).
     * Mục đích: Tạo response JSON với định dạng APIResponse và trả về client.
     *
     * @param request  Đối tượng HttpServletRequest chứa thông tin request từ client.
     * @param response Đối tượng HttpServletResponse để gửi response về client.
     * @param authException Lỗi xác thực từ Spring Security (thường do JwtAuthenticationProvider ném).
     * @throws IOException Nếu có lỗi khi ghi response.
     * @throws ServletException Nếu có lỗi liên quan đến Servlet.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // Lấy exception từ request (được set bởi CustomJwtDecoder hoặc filter)
        // CustomJwtDecoder gắn AppException (như TOKEN_EXPIRED_EXCEPTION, UNAUTHENTICATED) vào request
        Exception exception = (Exception) request.getAttribute("exception");

        // Khởi tạo đối tượng APIResponse để định dạng response
        APIResponse apiResponse = new APIResponse();

        // Kiểm tra xem exception có phải là AppException (từ CustomJwtDecoder) không
        if (exception instanceof AppException) {
            // Ép kiểu về AppException để lấy thông tin lỗi
            AppException appException = (AppException) exception;
            // Lấy mã lỗi (code) từ ErrorCode, ví dụ: 1002 (TOKEN_EXPIRED_EXCEPTION)
            apiResponse.setCode(appException.getErrorCode().getCode());
            // Lấy thông điệp lỗi, ví dụ: "Token expired exception"
            apiResponse.setMessage(appException.getErrorCode().getMessage());
        } else {
            // Nếu không có AppException (trường hợp lỗi xác thực chung từ Spring Security),
            // sử dụng mã lỗi mặc định UNAUTHENTICATED (1001)
            apiResponse.setCode(ErrorCode.UNAUTHENTICATED.getCode());
            apiResponse.setMessage(ErrorCode.UNAUTHENTICATED.getMessage());
        }

        // Thiết lập Content-Type của response là application/json
        response.setContentType("application/json");
        // Thiết lập mã trạng thái HTTP là 401 (Unauthorized), phù hợp với lỗi xác thực
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        // Chuyển APIResponse thành chuỗi JSON và ghi vào response
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }


}