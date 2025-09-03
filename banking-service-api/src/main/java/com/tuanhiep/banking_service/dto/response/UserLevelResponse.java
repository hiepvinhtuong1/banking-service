package com.tuanhiep.banking_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserLevelResponse {
    int id;
    String name;
    private int numberOfCards;
    private long dailyTransactionAmount;

}
