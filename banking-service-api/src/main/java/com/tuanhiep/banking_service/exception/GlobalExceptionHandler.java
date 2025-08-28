package com.tuanhiep.banking_service.exception;

import com.tuanhiep.banking_service.dto.response.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
/**
 * Global exception handler: Quản lý ngoại lệ toàn cục cho ứng dụng Spring Boot.
 */
public class GlobalExceptionHandler {

    // Xử lý các ngoại lệ khác các ngoại lệ đã được định nghĩa
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<APIResponse> handleException(Exception e) {
        APIResponse response = new APIResponse();
        response.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        response.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    // Xử lý ngoại lệ AppException
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<APIResponse> handleAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
       APIResponse response = new APIResponse();
       response.setCode(errorCode.getCode());
       response.setMessage(errorCode.getMessage());
       return ResponseEntity.badRequest().body(response);
    }

    // Xử lý ngoại lệ MethodArgumentNotValidException
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumKey);

        APIResponse response = new APIResponse();
        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(response);

    }
}
