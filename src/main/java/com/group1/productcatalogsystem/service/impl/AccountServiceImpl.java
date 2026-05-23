package com.group1.productcatalogsystem.service.impl;

import com.group1.productcatalogsystem.dto.request.AccountRequest;
import com.group1.productcatalogsystem.dto.response.AccountResponse;
import com.group1.productcatalogsystem.entity.Account;
import com.group1.productcatalogsystem.entity.AccountRole;
import com.group1.productcatalogsystem.exception.ResourceNotFoundException;
import com.group1.productcatalogsystem.mapper.AccountMapper;
import com.group1.productcatalogsystem.repository.AccountRepository;
import com.group1.productcatalogsystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService, UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        boolean isActive = account.getIsActive() == null || account.getIsActive();
        AccountRole role = account.getRole() != null ? account.getRole() : AccountRole.CUSTOMER;

        return new User(
                account.getUsername(),
                account.getPassword(),
                isActive,   // enabled
                true,       // accountNonExpired
                true,       // credentialsNonExpired
                true,       // accountNonLocked
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()))
        );
    }


    @Override
    public AccountResponse createAccount(AccountRequest request) {
        Account account = AccountMapper.toEntity(request);
        account.setPassword(request.getPassword());

        if (request.getRole() != null && !request.getRole().isBlank()) {
            account.setRole(AccountRole.valueOf(request.getRole().toUpperCase()));
        } else {
            account.setRole(AccountRole.CUSTOMER);
        }

        account.setFullName(request.getFullName());

        Account saved = accountRepository.save(account);
        return AccountMapper.toResponse(saved);
    }

    @Override
    public AccountResponse getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        return AccountMapper.toResponse(account);
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(AccountMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponse updateAccount(Long id, AccountRequest request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        account.setUsername(request.getUsername());
        account.setEmail(request.getEmail());
        account.setFullName(request.getFullName());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            account.setPassword(request.getPassword());
        }

        if (request.getRole() != null && !request.getRole().isBlank()) {
            account.setRole(AccountRole.valueOf(request.getRole().toUpperCase()));
        }

        Account updated = accountRepository.save(account);
        return AccountMapper.toResponse(updated);
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        accountRepository.delete(account);
    }
}
