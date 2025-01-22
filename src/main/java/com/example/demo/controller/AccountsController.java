package com.example.demo.controller;

import com.example.demo.dto.account.AccountDtoRequest;
import com.example.demo.dto.account.AccountDtoResponse;
import com.example.demo.model.Account;
import com.example.demo.service.AccountService;
import com.example.demo.service.AnimalService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private final AccountService accountService;

    private final AnimalService animalService;

    private final ModelMapper modelMapper;

    @Autowired
    public AccountsController(AccountService accountService, AnimalService animalService, ModelMapper modelMapper) {
        this.accountService = accountService;
        this.animalService = animalService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccountId(@PathVariable int accountId) {
        // Создаём форматтер, который выводит до 9 знаков после запятой (наносекунды).
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendInstant(9)
                .toFormatter();

        Instant now = Instant.now();
        String formatted = formatter.format(now);
        System.out.println(formatted);
        Account accountFounded = accountService.findAccountById(accountId);

        if (accountId <= 0) {
            return ResponseEntity.badRequest().body("");
        }

        if (accountFounded == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }

        return ResponseEntity.ok(convertAccountToAccountDtoResponse(accountFounded));
    }


    @GetMapping("/search")
    public ResponseEntity<?> searchAccount(@RequestParam(required = false) String firstName,
                                                  @RequestParam(required = false) String lastName,
                                                  @RequestParam(required = false) String email,
                                                  @RequestParam(defaultValue = "0") int from,
                                                  @RequestParam(defaultValue = "10") int size) {
        if (from < 0 || size <= 0) {
            return ResponseEntity.badRequest().body("");
        }

        return ResponseEntity.ok().body(accountService.searchAccount(firstName, lastName, email, from, size)
                .stream()
                .map(this::convertAccountToAccountDtoResponse)
                .toList());

    }

    @PutMapping("/{accountId}")
    public ResponseEntity<?> updateAccount(@PathVariable int accountId,
                                           @RequestBody AccountDtoRequest request, Principal principal) {
        if (accountService.isValideRegistrationAndUpdateRequest(request) || accountId <= 0) {
            return  ResponseEntity.badRequest().body("");
        }

        if (!request.getEmail().equals(principal.getName()) && accountService.isAccountExistByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("");
        }

        if (!accountService.isAccountExist(accountId) || !accountService.findAccountById(accountId).getEmail().equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        }

        return ResponseEntity.ok().body(convertAccountToAccountDtoResponse(accountService.updateAccount(accountId, request)));
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable int accountId,
                                           Principal principal) {

        if (accountId <= 0 || animalService.existAnimalsByChipper(accountService.findAccountById(accountId))) {
            return  ResponseEntity.badRequest().body("");
        }
        Account account = accountService.findAccountById(accountId);
        if (account == null || !account.getEmail().equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("");
        }

        accountService.deleteAccount(accountId);
        return ResponseEntity.ok("");
    }

    public AccountDtoResponse convertAccountToAccountDtoResponse(Account account) {
        return modelMapper.map(account, AccountDtoResponse.class);
    }


}
