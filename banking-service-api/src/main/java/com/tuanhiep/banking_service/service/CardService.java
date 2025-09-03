package com.tuanhiep.banking_service.service;


import com.tuanhiep.banking_service.dto.request.CardCreationRequest;
import com.tuanhiep.banking_service.dto.response.CardResponse;
import com.tuanhiep.banking_service.entity.Card;
import jakarta.validation.Valid;

public interface CardService {

    CardResponse createNew( CardCreationRequest request);
}

