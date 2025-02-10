package com.example.demo.service.impl;

import com.example.demo.dto.account.AccountDtoRequest;
import com.example.demo.dto.account.AccountDtoResponse;
import com.example.demo.dto.account.AccountDtoSearchRequest;
import com.example.demo.exceptions.ConflictDataException;
import com.example.demo.exceptions.ForbiddenException;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AnimalRepository;
import com.example.demo.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final AnimalRepository animalRepository;

    private final PasswordEncoder passwordEncoder;

    private final AccountMapper accountMapper;

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);


    @Override
    @Transactional
    public AccountDtoResponse save(AccountDtoRequest accountDTORequest, Principal principal) {
        Account account = accountMapper.toAccount(accountDTORequest);
        boolean isUserAuthenticated = principal != null;

        if (isUserAuthenticated) {
            throw new ForbiddenException("");
        }

        if (isAccountExistByEmail(account.getEmail())) {
            throw new ConflictDataException("");
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));

        return accountMapper.toAccountDtoResponse(accountRepository.save(account));

    }
    @Override
    public List<AccountDtoResponse> searchAccount(AccountDtoSearchRequest request) {
        List<Account> filtered = accountRepository.findAccountsByParams(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail());

        logger.debug("нашлось " + filtered.size() + " результатов");



        int toIndex = Math.min(filtered.size(), request.getFrom() + request.getSize());
        return filtered.subList(request.getFrom(), toIndex)
                .stream()
                .map(accountMapper::toAccountDtoResponse)
                .toList();
        
    }

    @Override
    @Transactional
    public AccountDtoResponse updateAccount(Integer accountId, AccountDtoRequest request, Principal principal) {
        boolean isAccountEmailAlreadyInUse = !request.getEmail().equals(principal.getName()) &&
                isAccountExistByEmail(request.getEmail());

        Account existAccount = accountRepository.findById(accountId).orElseThrow(() -> new ForbiddenException(""));

        boolean isUpdatingAccountBelongsToUser =
                existAccount
                .getEmail()
                .equals(principal.getName());


        if (isAccountEmailAlreadyInUse) {
            throw new ConflictDataException("");
        }


        if (!isUpdatingAccountBelongsToUser) {
            throw new ForbiddenException("");
        }

        existAccount.setFirstName(request.getFirstName());
        existAccount.setLastName(request.getLastName());
        existAccount.setEmail(request.getEmail());
        existAccount.setPassword(passwordEncoder.encode(request.getPassword()));

        return accountMapper.toAccountDtoResponse(accountRepository.save(existAccount));
    }

    @Override
    @Transactional
    public void deleteAccount(Integer accountId, Principal principal) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ForbiddenException(""));

        boolean isAccountBoundWithAnimal = animalRepository
                .existsByAccount(account);

        boolean isAccountBelongsToUser = account.getEmail().equals(principal.getName());

        if (isAccountBoundWithAnimal) {
            throw new RequestValidationException("");
        }



        if (!isAccountBelongsToUser) {
            throw new ForbiddenException("");
        }

        accountRepository.deleteById(accountId);
    }

    @Override
    public AccountDtoResponse findAccountById(Integer accountId) {
        return accountMapper.toAccountDtoResponse(accountRepository
                .findById(accountId)
                .orElseThrow(() -> new ObjectNotFoundException("")
                ));
    }

    private boolean isAccountExistByEmail(String email) {
        return (accountRepository.existsByEmail(email));
    }
}
