package com.example.demo.service.impl;


import com.example.demo.repository.AccountRepository;
import com.example.demo.security.MyUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {

    private AccountRepository accountRepository;

    private static final Logger log = LoggerFactory.getLogger(MyUserDetailsServiceImpl.class);


    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return accountRepository.findAccountByEmail(username)
                .map(MyUserDetails::new)
                .orElseThrow(() -> {
                    log.debug("User not found: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });
    }

}
