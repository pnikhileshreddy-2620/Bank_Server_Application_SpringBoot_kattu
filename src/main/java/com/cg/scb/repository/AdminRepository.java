package com.cg.scb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.scb.dto.Admin;
@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
	Admin findByUsername(String username);
}
