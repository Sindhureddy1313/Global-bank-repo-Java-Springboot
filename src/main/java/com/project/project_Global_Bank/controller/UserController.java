package com.project.project_Global_Bank.controller;

import com.project.project_Global_Bank.dto.*;
import com.project.project_Global_Bank.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User Account Management APIs")
public class UserController
{
    @Autowired
    UserService userService;

    @Operation(
            summary = "Create New User Account",
            description = "Creating a new user account and assigning ID to it "
    )
    @ApiResponse(
            responseCode = "201",
            description = "Http Status 201 created"
    )
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest)
    {
        return userService.createAccount(userRequest);
    }


    @Operation(
            summary = "Balance Enquiry for User Account",
            description = "Given an account fetching balance of user "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 201 success"
    )
    @GetMapping("/balanceEnquiry")
    public BankResponse balanceEnquiry(@RequestBody EnquiryRequest enquiryRequest)
    {
        return userService.balanceEnquiry(enquiryRequest);
    }


    @Operation(
            summary = "Name Enquiry for User Account",
            description = "Given an account fetching name of user "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 201 success"
    )
    @GetMapping("/nameEnquiry")
    public String nameEnquiry(@RequestBody EnquiryRequest enquiryRequest)
    {
        return userService.nameEnquiry(enquiryRequest);
    }

    @Operation(
            summary = "Amount credit into User Account",
            description = "Given an amount which is credited into user bank account "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 201 success"
    )
    @GetMapping("/creditAmount")
    public BankResponse creditAmount(@RequestBody CreditDebitRequest creditDebitRequest)
    {
        return userService.creditAccount(creditDebitRequest);
    }

    @Operation(
            summary = "Amount debit into User Account",
            description = "Given an amount which is debited from user bank account "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 201 success"
    )
    @GetMapping("/debitAmount")
    public BankResponse debitAmount(@RequestBody CreditDebitRequest creditDebitRequest)
    {
        return userService.debitAccount(creditDebitRequest);
    }

    @Operation(
            summary = "Amount credit into destination Account and debited from source account",
            description = "Given an amount which is debited from source bank account and credited into destination account "
    )
    @ApiResponse(
            responseCode = "200",
            description = "Http Status 201 success"
    )
    @GetMapping("/transfer")
    public BankResponse transfer(@RequestBody TransferRequest transferRequest)
    {
        return userService.transfer(transferRequest);
    }
}
