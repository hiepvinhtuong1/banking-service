package com.tuanhiep.banking_service.repository;

import com.tuanhiep.banking_service.entity.UserLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLeveLRepository extends JpaRepository<UserLevel,Integer> {
    boolean existsByName(String name);

    Optional<UserLevel> findByName(String vip);
}
