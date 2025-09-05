package com.tuanhiep.banking_service.service.impl.specification;

import com.tuanhiep.banking_service.entity.Transaction;
import com.tuanhiep.banking_service.enums.TransactionStatus;
import com.tuanhiep.banking_service.enums.TransactionType;
import org.springframework.data.jpa.domain.Specification;

public class TransactionSpecification {

    public static Specification<Transaction> filterByTransactionId(String transactionId) {
        return (root, query, criteriaBuilder) ->
                transactionId == null || transactionId.trim().isEmpty() ? null :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("transactionId")), "%" + transactionId.toLowerCase() + "%");
    }

    public static Specification<Transaction> filterByTransactionType(String transactionType) {
        return (root, query, criteriaBuilder) ->
                transactionType == null || transactionType.trim().isEmpty() ? null :
                        criteriaBuilder.equal(root.get("transactionType"), TransactionType.valueOf(transactionType));
    }

    public static Specification<Transaction> filterByTransactionStatus(String transactionStatus) {
        return (root, query, criteriaBuilder) ->
                transactionStatus == null || transactionStatus.trim().isEmpty() ? null :
                        criteriaBuilder.equal(root.get("transactionStatus"), TransactionStatus.valueOf(transactionStatus));
    }

    public static Specification<Transaction> combineFilters(String transactionId, String transactionType, String transactionStatus) {
        return Specification.where(filterByTransactionId(transactionId))
                .and(filterByTransactionType(transactionType))
                .and(filterByTransactionStatus(transactionStatus));
    }
}