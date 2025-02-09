package com.project.project_Global_Bank.utils;


import java.time.Year;


public class AccountUtils
{
    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account!!!";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "The account is created successfully";
    public static final String ACCOUNT_NOT_EXISTS_CODE = "003";
    public static final String ACCOUNT_NOT_EXISTS_MESSAGE = "Account number with Account NOT FOUND!!!!";
    public static final String ACCOUNT_FOUND_CODE  = "004";
    public static final String ACCOUNT_FOUND_SUCCESS = "User with Account number FOUND!!!";
    public static final String ACCOUNT_BALANCE_EXCEEDED = "005";
    public static final String ACCOUNT_BALANCE_EXCEEDED_MESSAGE = "No sufficient balance in your account";
    public static final String ACCOUNT_CREDIT_SUCCESS = "006";
    public static final String ACCOUNT_CREDIT_SUCCESS_MESSAGE = "Amount is credited successfully!!!!";
    public static final String ACCOUNT_DEBIT_SUCCESS = "007";
    public static final String ACCOUNT_DEBIT_SUCCESS_MESSAGE = "Amount is debited successfully!!!!!";
    public static final String ACCOUNT_TRANSFER_SUCCESS_CODE = "008";
    public static final String ACCOUNT_TRANSFER_SUCCESS_MESSAGE = "Transfer is successful";

    public static String generateAccountNumber()
    {
        /* 2025+ random 6 digits*/

//    generating account number

        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        //generate random number between min and max

        int randNumber = (int) Math.floor(Math.random() * (max-min+1) +min);
        //convert curr year and random number to strings, then concatenate

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);

        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randomNumber).toString();

    }


}
