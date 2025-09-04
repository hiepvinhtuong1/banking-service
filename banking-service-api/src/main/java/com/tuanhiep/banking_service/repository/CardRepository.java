package com.tuanhiep.banking_service.repository;

import com.tuanhiep.banking_service.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, String>, JpaSpecificationExecutor<Card> {
    Optional<Card> findCardByCardNumber(String cardNumber);
}
