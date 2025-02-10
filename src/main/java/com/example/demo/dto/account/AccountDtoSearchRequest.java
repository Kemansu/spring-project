package com.example.demo.dto.account;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AccountDtoSearchRequest {
    private String firstName;
    private String lastName;
    private String email;
    private Integer from;
    private Integer size;
}
