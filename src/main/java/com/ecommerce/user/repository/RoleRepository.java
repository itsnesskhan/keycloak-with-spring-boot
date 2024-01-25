package com.ecommerce.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.user.entity.UserRole;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Integer> {

}
