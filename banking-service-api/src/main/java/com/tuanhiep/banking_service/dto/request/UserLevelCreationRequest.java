package com.tuanhiep.banking_service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserLevelCreationRequest {
    @NotBlank(message = "USER_LEVEL_NAME_NOT_BLANK")
    String name;

    @NotNull(message = "Number of cards cannot be null")
    @Min(value = 0, message = "USER_LEVEL_NUMBER_OF_CARDS_INVALID")
    int numberOfCards;

    @NotNull(message = "Daily transaction amount cannot be null")
    @Min(value = 0, message = "USER_LEVEL_DAILY_AMOUNT_INVALID")
    long dailyTransactionAmount;
}
