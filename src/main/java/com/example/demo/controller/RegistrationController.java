package com.example.demo.controller;

import com.example.demo.dto.account.AccountDtoRequest;
import com.example.demo.dto.account.AccountDtoResponse;
import com.example.demo.model.Account;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

public interface RegistrationController {

    @PostMapping("/registration")
    ResponseEntity<AccountDtoResponse> register(@RequestBody @Valid AccountDtoRequest accountDTORequest, Principal principal) throws Exception;

}
