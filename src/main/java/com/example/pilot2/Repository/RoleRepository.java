package com.example.pilot2.Repository;


import com.example.pilot2.Entity.RoleEntity;
import com.example.pilot2.dto.constant.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByAuthority(Authority authority);
}
