package com.skyvault.repository;

import com.skyvault.model.Role;
import com.skyvault.model.RoleName;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, Integer> {

    Optional<Role> findByName(RoleName name);
}
