package com.tuanhiep.banking_service.mapper;

import com.tuanhiep.banking_service.dto.response.CardResponse;
import com.tuanhiep.banking_service.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(source = "account.accountId", target = "accountId")
    CardResponse toCardResponse(Card card);
}
