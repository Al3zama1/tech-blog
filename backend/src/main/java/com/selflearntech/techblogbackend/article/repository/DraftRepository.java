package com.selflearntech.techblogbackend.article.repository;

import com.selflearntech.techblogbackend.article.model.Draft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DraftRepository extends JpaRepository<Draft, Integer> {
}
