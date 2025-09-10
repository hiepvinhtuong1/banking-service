package com.tuanhiep.banking_service.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 1000 - Authentication
    UNAUTHENTICATED(1001, "Unauthenticated"),
    UNAUTHORIZED(1006, "Unthorized" ),
    TOKEN_EXPIRED_EXCEPTION(1002, "Token expired exception"),
    TOKEN_CANNOT_CREATE_EXCEPTION(1003,"Token can not create exception" ),
    EMAIL_OR_PASSWORD_NOT_MATCH(1004, "Email or password is incorrect"),
    ACCESS_TOKEN_STILL_VALID(1005,"Access token is still valid"),
    FORBIDDEN(1007,"Forbidden" ),
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
    USER_LEVEL_NOT_FOUND(2019,"User level not found"),
    ROLE_NOT_FOUND(2020,"Role not found"),
    ROLE_NOT_BLANK(2021,"Role is not blank"),
    USER_LEVEL_NOT_BLANK(2022,"User level is not blank"),
    ACCOUNT_HAS_BALANCE(2023, "Account has balance" ),
    ACCOUNT_HAS_CARDS(2024,"Account has cards"),

    // 3000 - Card
    CARD_NOT_FOUND(3001, "Card not found"),
    CARD_NUMBER_NOT_BLANK(3002, "Card number cannot be empty"),
    INVALID_CARD_NUMBER_FORMAT(3003, "Card number must contain only digits"),
    INVALID_CARD_NUMBER_LENGTH(3004, "Card number must be between 8 and 19 digits"),
    CARD_NUMBER_EXISTED(3005, "Card number already exists"),
    CARD_TYPE_NOT_NULL(3006, "Card type cannot be null"),
    EXPIRY_DATE_NOT_NULL(3007, "Expiry date cannot be null"),
    EXPIRY_DATE_MUST_BE_IN_FUTURE(3008, "Expiry date must be in the future"),
    CARD_STATUS_NOT_NULL(3009, "Card status cannot be null"),
    ACCOUNT_NOT_NULL(3010, "Account cannot be null"),
    CARD_IS_INACTIVE(3011, "Card is inactive"),
    CARD_IS_EXPIRED(3012, "Card is expired"),
    CARD_LIMIT_REACHED(3013,"Card limit reached" ),
    // 4000 - Balance
    BALANCE_NOT_FOUND(4001, "Balance not found"),
    BALANCE_ACCOUNT_NOT_NULL(4002, "Account cannot be null"),
    AVAILABLE_BALANCE_NOT_NEGATIVE(4003, "Available balance cannot be negative"),
    HOLD_BALANCE_NOT_NEGATIVE(4004, "Hold balance cannot be negative"),
    INSUFFICIENT_FUNDS(4005, "Insufficient funds"),
    BALANCE_ALREADY_EXISTS(4006, "Balance already exists for this account"),

    // 5000 - UserLevel
    USER_LEVEL_NAME_NOT_BLANK(5001, "User level name cannot be blank"),
    USER_LEVEL_NUMBER_OF_CARDS_INVALID(5002, "Number of cards must be >= 0"),
    USER_LEVEL_DAILY_AMOUNT_INVALID(5003, "Daily transaction amount must be >= 0"),
    USER_LEVEL_DUPLICATED(5004, "User level name already exists"),
    USER_LEVEL_IN_USE(5005, "User level is in use by accounts"),
    USER_LEVEL_NUMBER_OF_CARDS_NOT_NULL(5006,"Number of cards can not be null"),
    USER_LEVEL_DAILY_AMOUNT_NOT_NULL(5006,"Daily transaction amount can not be null"),

    // 6000 - Transaction
    TRANSACTION_NOT_FOUND(6001, "Transaction not found"),
    FROM_CARD_NUMBER_NOT_BLANK(6002, "From card number cannot be blank"),
    TO_CARD_NUMBER_NOT_BLANK(6003, "To card number cannot be blank"),
    INVALID_FROM_CARD_NUMBER_FORMAT(6004, "From card number must contain only digits"),
    INVALID_TO_CARD_NUMBER_FORMAT(6005, "To card number must contain only digits"),
    AMOUNT_NOT_NULL(6006, "Amount cannot be null"),
    AMOUNT_GREATER_THAN_ZERO(6007, "Amount must be greater than 0"),
    TRANSACTION_TYPE_NOT_NULL(6008, "Transaction type cannot be null"),
    TRANSACTION_STATUS_NOT_NULL(6009, "Transaction status cannot be null"),
    ACCOUNT_NOT_NULL_FOR_TRANSACTION(6010, "Account cannot be null for transaction"),
    FROM_TO_CARD_CANNOT_BE_SAME(6011, "From card and To card cannot be the same"),
    ACCOUNT_ID_NOT_BLANK(6012,"Account id not blank"),
    FROM_CARD_NOT_FOUND(6013,"From card is not found" ),
    TO_CARD_NOT_FOUND(6014,"To card is not found" ),
    FROM_CARD_INACTIVE(6015,"From card is not active"),
    TO_CARD_INACTIVE(6016,"To card is not active"),
    FROM_CARD_EXPIRED(6017, "FROM CARD EXPIRED"),
    TO_CARD_EXPIRED(6017, "TO CARD EXPIRED"),
    INSUFFICIENT_BALANCE(6019, "Balance is not sufficient"),
    DAILY_TRANSACTION_LIMIT_EXCEEDED(6020, "Daily transaction limit is exceeded"),
    OTP_CODE_NOT_BLANK(6021,"Otp code is not blank"),
    INVALID_OR_EXPIRED_OTP(6022,"Invalid or expired otp" ),
    INVALID_TRANSACTION_STATUS_PENDING(6023,"Invalid transaction status pending" ),
    // 9000 - System
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
