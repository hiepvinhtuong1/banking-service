package com.tuanhiep.notification_service.enums;

public enum TransactionStatus {
    PENDING,      // Chờ xử lý
    PROCESSING,   // Đang xử lý
    SUCCESS,      // Thành công
    FAILED,       // Thất bại
    CANCELLED,    // Bị hủy
    REVERSED;     // Đã hoàn tiền / đảo ngược

    // Lấy giá trị dạng String
    public String getValue() {
        return this.name();
    }
}
