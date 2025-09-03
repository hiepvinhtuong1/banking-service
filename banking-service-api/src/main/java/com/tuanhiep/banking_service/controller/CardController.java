package com.tuanhiep.banking_service.controller;

import com.tuanhiep.banking_service.dto.request.AccountCreationRequest;
import com.tuanhiep.banking_service.dto.request.AccountUpdateRequest;
import com.tuanhiep.banking_service.dto.request.CardCreationRequest;
import com.tuanhiep.banking_service.dto.response.APIResponse;
import com.tuanhiep.banking_service.dto.response.AccountResponse;
import com.tuanhiep.banking_service.dto.response.CardResponse;
import com.tuanhiep.banking_service.enums.CardStatus;
import com.tuanhiep.banking_service.enums.CardType;
import com.tuanhiep.banking_service.service.CardService;
import com.tuanhiep.banking_service.service.impl.AccountServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping
    public APIResponse<CardResponse> createNewCard(@RequestBody @Valid CardCreationRequest request) {
        return APIResponse.<CardResponse>builder()
                .data(cardService.createNew(request))
                .build();
    }

    @GetMapping("/types")
    public APIResponse<CardType[]> getCardTypes() {
        return APIResponse.<CardType[]>builder()
                .data(CardType.values()) // trả về mảng [DEBIT, CREDIT]
                .build();
    }

    @GetMapping("/status")
    public APIResponse<CardStatus[]> getCardStatus() {
        return APIResponse.<CardStatus[]>builder()
                .data(CardStatus.values()) // trả về mảng [DEBIT, CREDIT]
                .build();
    }

}
