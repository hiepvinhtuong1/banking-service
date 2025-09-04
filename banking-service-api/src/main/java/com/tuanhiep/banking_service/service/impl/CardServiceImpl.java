package com.tuanhiep.banking_service.service.impl;

import com.tuanhiep.banking_service.dto.request.CardCreationRequest;
import com.tuanhiep.banking_service.dto.response.CardResponse;
import com.tuanhiep.banking_service.entity.Account;
import com.tuanhiep.banking_service.entity.Card;
import com.tuanhiep.banking_service.entity.UserLevel;
import com.tuanhiep.banking_service.enums.CardStatus;
import com.tuanhiep.banking_service.exception.AppException;
import com.tuanhiep.banking_service.exception.ErrorCode;
import com.tuanhiep.banking_service.mapper.CardMapper;
import com.tuanhiep.banking_service.repository.AccountRepository;
import com.tuanhiep.banking_service.repository.CardRepository;
import com.tuanhiep.banking_service.service.CardService;
import com.tuanhiep.banking_service.service.impl.specification.CardSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardMapper cardMapper;

    @Override
    public CardResponse createNew(CardCreationRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        UserLevel userLevel = account.getUserLevel();
        if (userLevel == null) {
            throw new AppException(ErrorCode.USER_LEVEL_NOT_FOUND);
        }

        int maxCards = userLevel.getNumberOfCards();
        int currentCards = account.getCards().size();

        if (currentCards >= maxCards) {
            throw new AppException(ErrorCode.CARD_LIMIT_REACHED);
        }

        // generate card number random (8–19 digits)
        String cardNumber = generateCardNumber();

        Card card = Card.builder()
                .cardNumber(cardNumber)
                .cardType(request.getCardType())
                .expiryDate(request.getExpiryDate())
                .status(request.getStatus())
                .account(account)
                .build();

        return cardMapper.toCardResponse(cardRepository.save(card));
    }

    @Override
    public Page<CardResponse> getAllCards(String numberCard, String userName, Pageable pageable) {
        return cardRepository.findAll(
                        CardSpecification.filter(numberCard, userName),
                        pageable
                )
                .map(cardMapper::toCardResponse);
    }

    @Override
    public CardResponse getCard(String cardId) {
        var card = cardRepository.findById(cardId)
                .orElseThrow(() -> new AppException(ErrorCode.CARD_NOT_FOUND));

        if (!CardStatus.ACTIVE.toString().equalsIgnoreCase(card.getStatus().toString())) {
            throw new AppException(ErrorCode.CARD_IS_INACTIVE);
        }

        return cardMapper.toCardResponse(card);
    }

    @Override
    public CardResponse getCardByCardNumber(String cardNumber) {
        var card = cardRepository.findCardByCardNumber(cardNumber)
                .orElseThrow(() -> new AppException(ErrorCode.CARD_NOT_FOUND));

        if (!CardStatus.ACTIVE.toString().equalsIgnoreCase(card.getStatus().toString())) {
            throw new AppException(ErrorCode.CARD_IS_INACTIVE);
        }

        return cardMapper.toCardResponse(card);
    }


    private String generateCardNumber() {
        // Ví dụ sinh ngẫu nhiên 12 số, bạn có thể đổi logic này tùy yêu cầu
        return String.valueOf((long) (Math.random() * 1_0000_0000_0000L));
    }
}
