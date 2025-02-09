package com.project.project_Global_Bank.service.impl;

import com.project.project_Global_Bank.dto.TransactionDetails;
import com.project.project_Global_Bank.entity.Transaction;
import com.project.project_Global_Bank.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionImpl implements TransactionService
{
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransactions(TransactionDetails transactionDetails)
    {
        Transaction transaction = Transaction.builder()
                .transactionType(transactionDetails.getTransactionType())
                .accountNumber(transactionDetails.getAccountNumber())
                .amount(transactionDetails.getAmount())
                .status("SUCCESS")
                .build();

        transactionRepository.save(transaction);
        System.out.println("Transactions saved");
    }
}
