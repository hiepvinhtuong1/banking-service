package com.tuanhiep.banking_service.service;


import com.tuanhiep.banking_service.dto.request.CardCreationRequest;
import com.tuanhiep.banking_service.dto.response.CardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {

    CardResponse createNew( CardCreationRequest request);

    Page<CardResponse> getAllCards(String numberCard, String userName, Pageable pageable);

    CardResponse getCard(String cardId);

    CardResponse getCardByCardNumber(String cardNumber);
}

