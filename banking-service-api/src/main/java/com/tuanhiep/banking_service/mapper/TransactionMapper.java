package com.tuanhiep.banking_service.mapper;

import com.tuanhiep.banking_service.dto.response.TransactionResponse;
import com.tuanhiep.banking_service.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionResponse toTransactionResponse(Transaction transaction);
}
