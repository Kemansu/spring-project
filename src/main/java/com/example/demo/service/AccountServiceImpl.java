package com.example.demo.service;

import com.example.demo.dto.account.AccountDtoRequest;
import com.example.demo.dto.account.AccountDtoResponse;
import com.example.demo.exceptions.ConflictDataException;
import com.example.demo.exceptions.ForbiddenException;
import com.example.demo.exceptions.ObjectNotFoundException;
import com.example.demo.exceptions.RequestValidationException;
import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AnimalRepository;
import com.example.demo.serviceInterface.AccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);


    @Override
    @Transactional
    public AccountDtoResponse save(AccountDtoRequest accountDTORequest, Principal principal) {
        Account account = convertAccountRegisterDTOToAccount(accountDTORequest);
        if (principal != null) {
            throw new ForbiddenException("");
        }

        if (isAccountExistByEmail(account.getEmail())) {
            throw new ConflictDataException("");
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));

        return convertAccountToAccountDtoResponse(accountRepository.save(account));

    }
    @Override
    public List<Account> searchAccount(String firstName,
                                       String lastName,
                                       String email,
                                       int from,
                                       int size) throws RequestValidationException {
        List<Account> filtered = accountRepository.findAccountsByParams(
                firstName,
                lastName,
                email);

        logger.debug("нашлось " + filtered.size() + " результатов");

        int toIndex = Math.min(filtered.size(), from + size);
        return filtered.subList(from, toIndex);
        
    }

    @Override
    @Transactional
    public Account updateAccount(int accountId, AccountDtoRequest request, Principal principal)
            throws RequestValidationException, ConflictDataException, ForbiddenException {
        if (!request.getEmail().equals(principal.getName()) && isAccountExistByEmail(request.getEmail())) {
            throw new ConflictDataException("");
        }

        if (!isAccountExist(accountId) || !findAccountById(accountId).getEmail().equals(principal.getName())) {
            throw new ForbiddenException("");
        }



        Account existAccount = accountRepository.findById(accountId).orElse(null);

        existAccount.setFirstName(request.getFirstName());
        existAccount.setLastName(request.getLastName());
        existAccount.setEmail(request.getEmail());
        existAccount.setPassword(passwordEncoder.encode(request.getPassword()));

        return accountRepository.save(existAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(int accountId, Principal principal)
            throws RequestValidationException, ForbiddenException{



        if (animalRepository
                .existsByAccount(accountRepository.findById(accountId).orElseThrow(() -> new ForbiddenException("")))) {
            throw new RequestValidationException("");
        }

        Account account = findAccountById(accountId);


        if (account == null || !account.getEmail().equals(principal.getName())) {
            throw new ForbiddenException("");
        }

        accountRepository.deleteById(accountId);
    }

    @Override
    public boolean isAccountExist(int accountId) {
        return (accountRepository.existsById(accountId));
    }

    @Override
    public boolean isAccountExistByEmail(String email) {
        return (accountRepository.existsByEmail(email));
    }

    @Override
    public Account findAccountById(int accountId) throws RequestValidationException, ObjectNotFoundException{
        return accountRepository.findById(accountId).orElseThrow(() -> new ObjectNotFoundException(""));
    }

    @Override
    public Account convertAccountRegisterDTOToAccount(AccountDtoRequest accountDTORequest) {
        return modelMapper.map(accountDTORequest, Account.class);
    }

    @Override
    public AccountDtoResponse convertAccountToAccountDtoResponse(Account account) {
        return modelMapper.map(account, AccountDtoResponse.class);
    }
}
