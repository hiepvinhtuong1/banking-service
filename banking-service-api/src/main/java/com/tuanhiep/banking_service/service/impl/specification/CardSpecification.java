package com.tuanhiep.banking_service.service.impl.specification;

import com.tuanhiep.banking_service.entity.Card;
import org.springframework.data.jpa.domain.Specification;

public class CardSpecification {
    public static Specification<Card> filter(String numberCard, String userName) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();

            if (numberCard != null && !numberCard.isEmpty()) {
                predicate = cb.and(predicate,
                        cb.like(root.get("cardNumber"), "%" + numberCard + "%"));
            }
            if (userName != null && !userName.isEmpty()) {
                predicate = cb.and(predicate,
                        cb.like(root.get("userName"), "%" + userName + "%"));
            }
            return predicate;
        };
    }
    }