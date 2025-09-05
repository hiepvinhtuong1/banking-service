package com.tuanhiep.banking_service.enums;

public enum TransactionType {
    DEPOSIT,
    WITHDRAW, TRANSFER;

    public String getValue() {
        return this.name();
    }
}
