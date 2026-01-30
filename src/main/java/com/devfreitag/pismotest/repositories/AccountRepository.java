package com.devfreitag.pismotest.repositories;

import com.devfreitag.pismotest.entities.Account;
import com.devfreitag.pismotest.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {


}
