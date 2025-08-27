package com.tuanhiep.banking_service.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),

    UNAUTHENTICATED(1001,"Unauthenticated"),

    ACCOUNT_EXISTED(2001, "Account existed"),
    INVALID_EMAIL(2002, "Email must be a valid email address"),
    EMAIL_NOT_BLANK(2003, "Email cannot be empty"),
    INVALID_PHONE_NUMBER_LENGTH(2004, "Phone number must be between 10 and 15 characters"),
    PHONE_NUMBER_NOT_BLANK(2005, "Phone number cannot be empty"),
    INVALID_PASSWORD_LENGTH(2006, "Password must be greater than 6 characters"),
    PASSWORD_NOT_BLANK(2007, "Password cannot be empty"),
    CUSTOMER_NAME_NOT_BLANK(2008, "Customer name cannot be empty"),
    ACCOUNT_NOT_FOUND(2009,"Account not found")
    ;
    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
