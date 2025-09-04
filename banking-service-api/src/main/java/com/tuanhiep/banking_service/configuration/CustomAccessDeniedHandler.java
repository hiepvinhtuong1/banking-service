package com.tuanhiep.banking_service.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuanhiep.banking_service.dto.response.APIResponse;
import com.tuanhiep.banking_service.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        APIResponse apiResponse = new APIResponse();
        apiResponse.setCode(ErrorCode.UNAUTHORIZED.getCode()); // ví dụ code cho không đủ quyền
        apiResponse.setMessage("Bạn không có quyền truy cập tài nguyên này");

        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value()); // 403
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}

