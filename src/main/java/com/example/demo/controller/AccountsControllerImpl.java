package com.example.demo.controller;

import com.example.demo.controllerInterface.AccountsController;
import com.example.demo.dto.account.AccountDtoRequest;
import com.example.demo.dto.account.AccountDtoResponse;
import com.example.demo.exceptions.ConflictDataException;
import com.example.demo.exceptions.ForbiddenException;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
import com.example.demo.model.Account;
import com.example.demo.serviceInterface.AccountService;
import com.example.demo.serviceInterface.AnimalService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountsControllerImpl implements AccountsController {

    private final AccountService accountServiceImpl;

    private final ModelMapper modelMapper;



    @Override
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountDtoResponse> getAccountId(@PathVariable @Min(value = 1) int accountId) {
        Account accountFounded = accountServiceImpl.findAccountById(accountId);


        return ResponseEntity.ok(convertAccountToAccountDtoResponse(accountFounded));
    }


    @Override
    @GetMapping("/search")
    public ResponseEntity<List<AccountDtoResponse>> searchAccount(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(defaultValue = "0") @Min(value = 0) int from,
            @RequestParam(defaultValue = "10") @Min(value = 1) int size) {
        return ResponseEntity.ok().body(accountServiceImpl.searchAccount(firstName, lastName, email, from, size)
                .stream()
                .map(this::convertAccountToAccountDtoResponse)
                .toList());

    }

    @Override
    @PutMapping("/{accountId}")
    public ResponseEntity<AccountDtoResponse> updateAccount(
            @PathVariable @Min(value = 1) int accountId,
            @RequestBody @Valid  AccountDtoRequest request, Principal principal) {
        return ResponseEntity
                .ok()
                .body(convertAccountToAccountDtoResponse(
                        accountServiceImpl.updateAccount(accountId, request, principal)
                ));
    }

    @Override
    @DeleteMapping("/{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable @Min(value = 1) int accountId,
                                           Principal principal) {


        accountServiceImpl.deleteAccount(accountId, principal);
        return ResponseEntity.ok("");
    }

    @Override
    public AccountDtoResponse convertAccountToAccountDtoResponse(Account account) {
        return modelMapper.map(account, AccountDtoResponse.class);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> handleObjectNotFoundException(ObjectNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<String> handleIllegalArgumentException(RequestValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ConflictDataException.class)
    public ResponseEntity<String> handleConflictDataException(ConflictDataException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> handleConflictDataException(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }


}
