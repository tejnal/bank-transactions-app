package com.bank.app.repository;

import com.bank.app.data.entity.Deposits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposits, Long> {

    List<Deposits> findTransactionsByCustomerAccountId(long id);
}
