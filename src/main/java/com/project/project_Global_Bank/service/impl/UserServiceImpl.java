package com.project.project_Global_Bank.service.impl;

import com.project.project_Global_Bank.dto.*;
import com.project.project_Global_Bank.entity.User;
import com.project.project_Global_Bank.repository.UserRepository;
import com.project.project_Global_Bank.utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        //checking if user has account already
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        //creating a new user
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .status("ACTIVE")
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .phoneNumber(userRequest.getPhoneNumber())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .build();

        User savedUser = userRepository.save(newUser);
        //send email Alert to the saveduser email
        EmailDetails emailDetails = EmailDetails.builder()
                .subject("Account successfully created in Global Bank!!!!")
                .messageBody("Congratulations" + " " + savedUser.getFirstName() + " " + "on successfully creating account at our bank. You are now most valued customer!!!! Your account details are here : \nAccount name:" + " " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + "\nYour Account number:" + " " + savedUser.getAccountNumber())
                .recipient(savedUser.getEmail())
                .build();
        emailService.sendEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                        .accountNumber(savedUser.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        if (userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber())) {
            User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                    .accountInfo(AccountInfo.builder()
                            .accountName(foundUser.getFirstName() + " " + foundUser.getLastName())
                            .accountNumber(foundUser.getAccountNumber())
                            .accountBalance(foundUser.getAccountBalance())
                            .build())
                    .build();
        } else {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        if (userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber())) {
            User namedUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
            return AccountUtils.ACCOUNT_FOUND_SUCCESS + namedUser.getFirstName() + " " + namedUser.getLastName();
        } else {
            return AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE;
        }
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        Boolean isAccountPresent = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (!isAccountPresent) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        else
        {
            User user = userRepository.findByAccountNumber((creditDebitRequest.getAccountNumber()));
            user.setAccountBalance(user.getAccountBalance().add(creditDebitRequest.getAmount()));
            userRepository.save(user);

            TransactionDetails transactionDetails = TransactionDetails.builder()
                    .accountNumber(user.getAccountNumber())
                    .transactionType("CREDITED")
                    .amount(creditDebitRequest.getAmount())
                    .build();

            transactionService.saveTransactions(transactionDetails);


            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_CREDIT_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_CREDIT_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(user.getFirstName() + " " + user.getLastName())
                            .accountNumber(user.getAccountNumber())
                            .accountBalance(user.getAccountBalance())
                            .build())
                    .build();
        }
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
        Boolean isAccountPresent = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (Boolean.FALSE.equals(isAccountPresent)) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        else
        {
            User user = userRepository.findByAccountNumber((creditDebitRequest.getAccountNumber()));
            if (user.getAccountBalance().compareTo(creditDebitRequest.getAmount()) > 0) {
                user.setAccountBalance(user.getAccountBalance().subtract(creditDebitRequest.getAmount()));
                userRepository.save(user);
                TransactionDetails transactionDetails = TransactionDetails.builder()
                        .accountNumber(user.getAccountNumber())
                        .transactionType("DEBITED")
                        .amount(creditDebitRequest.getAmount())
                        .build();

                transactionService.saveTransactions(transactionDetails);
            }
            else
            {
                return BankResponse.builder()
                        .responseCode(AccountUtils.ACCOUNT_BALANCE_EXCEEDED)
                        .responseMessage(AccountUtils.ACCOUNT_BALANCE_EXCEEDED_MESSAGE)
                        .accountInfo(AccountInfo.builder()
                                .accountName(user.getFirstName() + " " + user.getLastName())
                                .accountNumber(user.getAccountNumber())
                                .accountBalance(user.getAccountBalance())
                                .build())
                        .build();
            }
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBIT_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBIT_SUCCESS_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(user.getFirstName() + " " + user.getLastName())
                            .accountNumber(user.getAccountNumber())
                            .accountBalance(user.getAccountBalance())
                            .build())
                    .build();
        }
    }

    @Override
    public BankResponse transfer(TransferRequest request)
    {
        Boolean isDestinationAccountPresent = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if (Boolean.FALSE.equals(isDestinationAccountPresent)) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        User sourceUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
        if (sourceUser.getAccountBalance().compareTo(request.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_BALANCE_EXCEEDED)
                    .responseMessage(AccountUtils.ACCOUNT_BALANCE_EXCEEDED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(sourceUser.getFirstName() + " " + sourceUser.getLastName())
                            .accountNumber(sourceUser.getAccountNumber())
                            .accountBalance(sourceUser.getAccountBalance())
                            .build())
                    .build();
        }

        sourceUser.setAccountBalance(sourceUser.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(sourceUser);
        EmailDetails debitAlert = EmailDetails.builder()
                .subject("DEBIT ALERT")
                .recipient(sourceUser.getEmail())
                .messageBody("The amount" + " " + request.getAmount() + " " + "has been debited from your account" + sourceUser.getAccountNumber() + " remaining balance: " + sourceUser.getAccountBalance())
                .build();

        emailService.sendEmailAlert(debitAlert);

        User destinationUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
        destinationUser.setAccountBalance(destinationUser.getAccountBalance().add(request.getAmount()));
        userRepository.save(destinationUser);
        EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(sourceUser.getEmail())
                .messageBody("The amount" + " " + request.getAmount() + " " + "has been credited to your account" + destinationUser.getAccountNumber() + "from" + sourceUser.getFirstName() + sourceUser.getLastName() + " remaining balance: " + destinationUser.getAccountBalance())
                .build();

        emailService.sendEmailAlert(creditAlert);

        TransactionDetails transactionDetails = TransactionDetails.builder()
                .accountNumber(destinationUser.getAccountNumber())
                .transactionType("CREDITED")
                .amount(request.getAmount())
                .build();

        transactionService.saveTransactions(transactionDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_TRANSFER_SUCCESS_MESSAGE)
                .accountInfo(null)
                .build();


    }



}



