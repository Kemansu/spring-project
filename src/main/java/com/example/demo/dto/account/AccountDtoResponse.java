package com.example.demo.dto.account;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountDtoResponse {

    private int id;

    private String firstName;

    private String lastName;

    private String email;

    public AccountDtoResponse() {
    }

}
