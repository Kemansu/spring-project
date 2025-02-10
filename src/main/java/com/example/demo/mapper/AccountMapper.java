package com.example.demo.mapper;

import com.example.demo.dto.account.AccountDtoRequest;
import com.example.demo.dto.account.AccountDtoResponse;
import com.example.demo.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountDtoResponse toAccountDtoResponse(Account account);

    @Mapping(target = "id", ignore = true)
    Account toAccount(AccountDtoRequest accountDTORequest);
}
