package com.selflearntech.techblogbackend.user.repository;

import com.selflearntech.techblogbackend.user.model.Role;
import com.selflearntech.techblogbackend.user.model.RoleType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByAuthority(RoleType role);
}
