package com.project.project_Global_Bank.controller;

import com.itextpdf.text.DocumentException;
import com.project.project_Global_Bank.entity.Transaction;
import com.project.project_Global_Bank.service.impl.BankStatementService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/bankstatement")
@AllArgsConstructor
public class TransactionController
{
    private BankStatementService bankStatementService;

    @GetMapping
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber,@RequestParam String fromDate, @RequestParam String toDate) throws DocumentException, FileNotFoundException {
        return bankStatementService.generateStatement(accountNumber,fromDate,toDate);
    }
}
