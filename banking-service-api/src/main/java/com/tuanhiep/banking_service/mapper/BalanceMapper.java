package com.tuanhiep.banking_service.mapper;

import com.tuanhiep.banking_service.dto.response.BalanceResponse;
import com.tuanhiep.banking_service.entity.Balance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BalanceMapper {

    BalanceResponse toBalanceResponse(Balance balance);
}
