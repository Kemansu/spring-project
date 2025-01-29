package com.example.demo.service;

import com.example.demo.dto.account.AccountDtoRequest;
import com.example.demo.dto.account.AccountDtoResponse;
import com.example.demo.model.Account;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;


public interface AccountService {


    AccountDtoResponse save(AccountDtoRequest accountDTORequest, Principal principal) throws Exception;

    List<Account> searchAccount(String firstName,
                                       String lastName,
                                       String email,
                                       int from,
                                       int size);

    Account updateAccount(int accountId, AccountDtoRequest request, Principal principal);


    void deleteAccount(int accountId, Principal principal);

    boolean isAccountExist(int accountId);

    boolean isAccountExistByEmail(String email);

    Account findAccountById(int id);

}
