package com.example.demo.controller;

import com.example.demo.dto.account.AccountDtoRequest;
import com.example.demo.dto.account.AccountDtoResponse;
import com.example.demo.model.Account;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

public interface AccountsController {

    @GetMapping("/{accountId}")
    ResponseEntity<AccountDtoResponse> getAccountId(@PathVariable @Min(value = 1) int accountId);

    @GetMapping("/search")
    ResponseEntity<List<AccountDtoResponse>> searchAccount(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") @Min(value = 0) int from,
            @RequestParam(defaultValue = "10") @Min(value = 1)int size);

    @PutMapping("/{accountId}")
    ResponseEntity<AccountDtoResponse> updateAccount(@PathVariable @Min(value = 1) int accountId,
                                    @RequestBody @Valid AccountDtoRequest request,
                                    Principal principal);

    @DeleteMapping("/{accountId}")
    ResponseEntity<String> deleteAccount(@PathVariable @Min(value = 1) int accountId,
                                    Principal principal);
}
