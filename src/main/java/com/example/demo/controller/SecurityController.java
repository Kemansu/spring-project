package com.example.demo.controller;

import com.example.demo.dto.account.AccountDtoRequest;
import com.example.demo.dto.account.AccountDtoResponse;
import com.example.demo.model.Account;
import com.example.demo.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class SecurityController {

    private final ModelMapper modelMapper;

    private final AccountService accountService;


    @Autowired
    public SecurityController(ModelMapper modelMapper, AccountService accountService) {
        this.modelMapper = modelMapper;
        this.accountService = accountService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> register(@RequestBody AccountDtoRequest accountDTORequest, Principal principal) throws Exception {
        Account account = convertAccountRegisterDTOToAccount(accountDTORequest);

        if (principal != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        }

        if (accountService.isValideRegistrationAndUpdateRequest(accountDTORequest)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
        }

        if (accountService.isAccountExistByEmail(account.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("");
        }



        return ResponseEntity.status(HttpStatus.CREATED).body(convertAccountToAccountDtoResponse(accountService.save(account)));
    }


    public Account convertAccountRegisterDTOToAccount(AccountDtoRequest accountDTORequest) {
        return modelMapper.map(accountDTORequest, Account.class);
    }

    public AccountDtoResponse convertAccountToAccountDtoResponse(Account account) {
        return modelMapper.map(account, AccountDtoResponse.class);
    }

}
