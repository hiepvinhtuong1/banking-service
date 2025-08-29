package com.tuanhiep.banking_service.service.impl.specification;

import com.tuanhiep.banking_service.entity.Account;
import org.springframework.data.jpa.domain.Specification;

public class AccountSpecification {

    public static Specification<Account> filterByCustomerName(String customerName) {
        return (root, query, criteriaBuilder) ->
                customerName == null ? null :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("customerName")), "%" + customerName.toLowerCase() + "%");
    }

    public static Specification<Account> filterByPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) ->
                phoneNumber == null ? null :
                        criteriaBuilder.like(root.get("phoneNumber"), "%" + phoneNumber + "%");
    }

    public static Specification<Account> filterByEmail(String email) {
        return (root, query, criteriaBuilder) ->
                email == null ? null :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%");
    }

    public static Specification<Account> combineFilters(String customerName, String phoneNumber, String email) {
        return Specification.where(filterByCustomerName(customerName))
                .and(filterByPhoneNumber(phoneNumber))
                .and(filterByEmail(email));
    }
}
