package com.example.demo.controller.impl;

import com.example.demo.controller.AccountsController;
import com.example.demo.dto.account.AccountDtoRequest;
import com.example.demo.dto.account.AccountDtoResponse;
import com.example.demo.dto.account.AccountDtoSearchRequest;
import com.example.demo.service.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Tag(name = "Accounts", description = "Управление учетными записями пользователей")
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountsControllerImpl implements AccountsController {

    private final AccountService accountServiceImpl;

    @Override
    public ResponseEntity<AccountDtoResponse> getAccountId(@PathVariable Integer accountId) {
        return ResponseEntity.ok(accountServiceImpl.findAccountById(accountId));
    }

    @Override
    public ResponseEntity<List<AccountDtoResponse>> searchAccount(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") Integer from,
            @RequestParam(defaultValue = "10") Integer size) {

        AccountDtoSearchRequest dtoSearchRequest = AccountDtoSearchRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .from(from)
                .size(size)
                .build();

        return ResponseEntity.ok().body(accountServiceImpl.searchAccount(dtoSearchRequest));
    }

    @Override
    public ResponseEntity<AccountDtoResponse> updateAccount(
            @PathVariable Integer accountId,
            @RequestBody AccountDtoRequest request,
            Principal principal) {
        return ResponseEntity
                .ok()
                .body(accountServiceImpl.updateAccount(accountId, request, principal));
    }

    @Override
    public ResponseEntity<Void> deleteAccount(@PathVariable Integer accountId, Principal principal) {
        accountServiceImpl.deleteAccount(accountId, principal);
        return ResponseEntity.ok().build();
    }
}