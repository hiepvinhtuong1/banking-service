// validators.js
export const FIELD_REQUIRED_MESSAGE = 'This field is required.'

// Email
export const EMAIL_RULE = /^\S+@\S+\.\S+$/
export const EMAIL_RULE_MESSAGE = 'Email is invalid. (example@trungquandev.com)'

// Phone number (10 - 15 digits)
export const PHONE_RULE = /^[0-9]{10,15}$/
export const PHONE_RULE_MESSAGE = 'Phone number must contain 10-15 digits.'

// Password
export const PASSWORD_RULE = /^(?=.*[a-zA-Z])(?=.*\d)[A-Za-z\d\W]{8,256}$/
export const PASSWORD_RULE_MESSAGE =
  'Password must include at least 1 letter, 1 number, and be 8-256 characters long.'

// Password confirmation
export const PASSWORD_CONFIRMATION_MESSAGE = 'Password Confirmation does not match!'
