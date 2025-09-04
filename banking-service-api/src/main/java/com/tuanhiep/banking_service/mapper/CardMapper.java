package com.tuanhiep.banking_service.mapper;

import com.tuanhiep.banking_service.dto.response.CardResponse;
import com.tuanhiep.banking_service.entity.Card;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(source = "account.accountId", target = "accountId")
    @Mapping(source = "account.email", target = "email")
    @Mapping(source = "account.customerName", target = "customerName")
    @Mapping(source = "account.phoneNumber", target = "phoneNumber")
    CardResponse toCardResponse(Card card);


}
