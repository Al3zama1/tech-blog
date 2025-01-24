package com.selflearntech.techblogbackend.user.repository;

import com.selflearntech.techblogbackend.token.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
}
