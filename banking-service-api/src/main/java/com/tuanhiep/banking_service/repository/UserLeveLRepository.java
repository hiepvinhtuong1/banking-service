package com.tuanhiep.banking_service.repository;

import com.tuanhiep.banking_service.entity.UserLevel;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLeveLRepository extends JpaRepository<UserLevel,Integer> {
    boolean existsByName(String name);
}
