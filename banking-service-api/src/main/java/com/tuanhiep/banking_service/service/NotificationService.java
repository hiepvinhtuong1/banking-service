package com.tuanhiep.banking_service.service;

import com.tuanhiep.banking_service.dto.response.TransactionResponse;
import com.tuanhiep.banking_service.entity.Transaction;

public interface NotificationService {
    void notifyAdminPendingTransaction(TransactionResponse transaction);
}
