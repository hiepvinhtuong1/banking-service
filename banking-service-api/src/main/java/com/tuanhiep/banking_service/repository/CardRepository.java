package com.tuanhiep.banking_service.repository;

import com.tuanhiep.banking_service.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card,String> {
}
