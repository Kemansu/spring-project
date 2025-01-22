package com.example.demo.service;

import com.example.demo.dto.account.AccountDtoRequest;
import com.example.demo.model.Account;
import com.example.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Account save(Account account) throws Exception {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        try {
            return accountRepository.save(account);
        } catch (Exception e) {
            throw new Exception();
        }
    }

    public List<Account> searchAccount(String firstName,
                                       String lastName,
                                       String email,
                                       int from,
                                       int size) {
        List<Account> filtered = accountRepository.findAll().stream()
                .filter(account ->
                        (firstName == null || account.getFirstName().toLowerCase().contains(firstName.toLowerCase())) &&
                                (lastName == null || account.getLastName().toLowerCase().contains(lastName.toLowerCase())) &&
                                (email == null || account.getEmail().toLowerCase().contains(email.toLowerCase()))
                )
                .sorted(Comparator.comparing(Account::getId))
                .toList();


        int toIndex = Math.min(filtered.size(), from + size);
        return filtered.subList(from, toIndex);
        
    }

    @Transactional
    public Account updateAccount(int accountId, AccountDtoRequest request) {
        Account existAccount = accountRepository.findById(accountId).orElse(null);

        existAccount.setFirstName(request.getFirstName());
        existAccount.setLastName(request.getLastName());
        existAccount.setEmail(request.getEmail());
        existAccount.setPassword(passwordEncoder.encode(request.getPassword()));

        return accountRepository.save(existAccount);
    }

    @Transactional
    public void deleteAccount(int accountId) {
        accountRepository.deleteById(accountId);
    }

    public boolean isAccountExist(int accountId) {
        return (accountRepository.existsById(accountId));
    }

    public boolean isAccountExistByEmail(String email) {
        return (accountRepository.existsByEmail(email));
    }

    public boolean isValideRegistrationAndUpdateRequest(AccountDtoRequest request) {
        return (request.getFirstName() == null ||
                request.getFirstName().trim().isEmpty() ||
                request.getLastName() == null ||
                request.getLastName().trim().isEmpty() ||
                request.getEmail() == null ||
                request.getEmail().trim().isEmpty() ||
                !request.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$") ||
                request.getPassword() == null ||
                request.getPassword().trim().isEmpty());
    }

    public Account findAccountById(int id) {
        return accountRepository.findById(id).orElse(null);
    }
}
