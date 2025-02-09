package com.project.project_Global_Bank.service.impl;

import com.project.project_Global_Bank.dto.TransactionDetails;
import com.project.project_Global_Bank.entity.Transaction;

public interface TransactionService
{
    void saveTransactions(TransactionDetails transactionDetails);
}
