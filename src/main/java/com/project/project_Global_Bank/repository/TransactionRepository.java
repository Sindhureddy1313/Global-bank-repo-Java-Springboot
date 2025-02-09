package com.project.project_Global_Bank.repository;

import com.project.project_Global_Bank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction,String> {
}
