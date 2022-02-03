package com.prorent.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prorent.carrental.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{

}
