package com.cg.scb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.scb.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

}
