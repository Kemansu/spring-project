package com.example.demo.controller.impl;

import com.example.demo.controller.RegistrationController;
import com.example.demo.dto.account.AccountDtoRequest;
import com.example.demo.dto.account.AccountDtoResponse;
import com.example.demo.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Tag(name = "Registration", description = "Регистрация новых пользователей")
@RestController
@RequiredArgsConstructor
public class RegistrationControllerImpl implements RegistrationController {


    private final AccountService accountServiceImpl;



    @Override
    @PostMapping("/registration")
    @Operation(
            summary = "Зарегистрироваться",
            description = "Регистрирует новых пользователй")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Запрос усешно выполнен"),
            @ApiResponse(responseCode = "400", description = "Невалидные данные для регистрации"),
            @ApiResponse(responseCode = "403", description = "Запрос от авторизованного аккаунта"),
            @ApiResponse(responseCode = "409", description = "Аккаунт с таким email уже существует")
    })
    public ResponseEntity<AccountDtoResponse> register(@RequestBody @Valid AccountDtoRequest accountDTORequest,
                                                       Principal principal) throws Exception {

        return ResponseEntity.status(HttpStatus.CREATED).body(accountServiceImpl.save(accountDTORequest, principal));
    }

}
