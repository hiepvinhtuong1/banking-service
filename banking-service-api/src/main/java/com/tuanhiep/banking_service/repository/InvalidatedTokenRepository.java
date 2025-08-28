package com.tuanhiep.banking_service.repository;

import com.tuanhiep.banking_service.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
