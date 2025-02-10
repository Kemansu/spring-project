package com.example.demo.service;

import com.example.demo.dto.account.AccountDtoRequest;
import com.example.demo.dto.account.AccountDtoResponse;
import com.example.demo.dto.account.AccountDtoSearchRequest;
import com.example.demo.model.Account;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;


public interface AccountService {


    AccountDtoResponse save(AccountDtoRequest accountDTORequest, Principal principal);

    List<AccountDtoResponse> searchAccount(AccountDtoSearchRequest request);

    AccountDtoResponse updateAccount(Integer accountId, AccountDtoRequest request, Principal principal);

    void deleteAccount(Integer accountId, Principal principal);

    AccountDtoResponse findAccountById(Integer id);

}
