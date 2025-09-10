package com.tuanhiep.banking_service.repository;

import com.tuanhiep.banking_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {

    @Query("SELECT COALESCE(SUM(t.amount), 0) " +
            "FROM transactions t " +
            "WHERE t.fromAccountId = :accountId " +
            "AND FUNCTION('DATE', t.createdAt) = :today " +
            "AND t.transactionStatus = 'PENDING'")
    BigDecimal sumTransactionAmountByAccountAndDate(@Param("accountId") String accountId,
                                                    @Param("today") LocalDate today);
}
