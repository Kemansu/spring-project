package com.example.demo.controller;

import com.example.demo.dto.account.AccountDtoRequest;
import com.example.demo.dto.account.AccountDtoResponse;
import com.example.demo.model.Account;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

public interface RegistrationController {

    @Operation(
            summary = "Зарегистрироваться",
            description = "Регистрирует новых пользователй")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Запрос усешно выполнен"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные для регистрации"),
            @ApiResponse(responseCode = "403", description = "Запрос от авторизованного аккаунта"),
            @ApiResponse(responseCode = "409", description = "Аккаунт с таким email уже существует")
    })
    @PostMapping("/registration")
    ResponseEntity<AccountDtoResponse> register(
            @RequestBody @Valid AccountDtoRequest accountDTORequest,
            Principal principal);

}
