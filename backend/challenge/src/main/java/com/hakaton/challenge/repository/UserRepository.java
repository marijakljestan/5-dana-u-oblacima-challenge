package com.hakaton.challenge.repository;

import com.hakaton.challenge.domain.UserEntity;
import com.hakaton.challenge.domain.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}
