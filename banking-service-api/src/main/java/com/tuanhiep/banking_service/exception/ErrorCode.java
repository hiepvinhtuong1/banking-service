package com.tuanhiep.banking_service.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 1000 - Authentication
    UNAUTHENTICATED(1001, "Unauthenticated"),
    TOKEN_EXPIRED_EXCEPTION(1002, "Token expired exception"),
    TOKEN_CANNOT_CREATE_EXCEPTION(1003,"Token can not create exception" ),
    EMAIL_OR_PASSWORD_NOT_MATCH(1004, "Email or passowrd is incorrect"),
    ACCESS_TOKEN_STILL_VALID(1005,"access token is still valid"),
    // 2000 - Account
    ACCOUNT_EMAIL_EXISTED(2001, "Email existed"),
    ACCOUNT_PHONE_NUMBER_EXISTED(2002, "Phone number existed"),
    INVALID_EMAIL(2003, "Email must be a valid email address"),
    EMAIL_NOT_BLANK(2004, "Email cannot be empty"),
    INVALID_PHONE_NUMBER_LENGTH(2005, "Phone number must be between 10 and 15 characters"),
    PHONE_NUMBER_NOT_BLANK(2006, "Phone number cannot be empty"),
    INVALID_PASSWORD_LENGTH(2007, "Password must be greater than 6 characters"),
    PASSWORD_NOT_BLANK(2008, "Password cannot be empty"),
    CUSTOMER_NAME_NOT_BLANK(2009, "Customer name cannot be empty"),
    ACCOUNT_NOT_FOUND(2010, "Account not found"),
    ACCOUNT_IS_ACTIVE(2011,"Account is active"),
    VERIFY_CODE_NOT_MATCH(2012,"Verify code does not match"),
    EMAIL_INVALID_FORMAT(2013, "Email format is invalid (example: example@domain.com)"),
    INVALID_PHONE_NUMBER_FORMAT(2014, "Phone number must contain only digits"),
    INVALID_PASSWORD_FORMAT(2015, "Password must include at least 1 letter, 1 number and be 8-256 characters long"),
    VERIFY_CODE_NOT_BLANK(2016,"Verify code is not blank"),
    ACCOUNT_IS_NOT_ACTIVE(2017,"Account is not active"),
    ACCOUNT_IS_DELETED(2018,"Account is deleted"),
    // 9000 - System
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"), ;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
