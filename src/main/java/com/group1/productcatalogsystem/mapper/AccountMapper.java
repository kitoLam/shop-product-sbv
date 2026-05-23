package com.group1.productcatalogsystem.mapper;

import com.group1.productcatalogsystem.dto.request.AccountRequest;
import com.group1.productcatalogsystem.dto.response.AccountResponse;
import com.group1.productcatalogsystem.entity.Account;

public class AccountMapper {

    private AccountMapper() {
        // Utility class — prevent instantiation
    }

    public static AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .username(account.getUsername())
                .email(account.getEmail())
                .fullName(account.getFullName())
                .role(account.getRole().name())
                .build();
    }

    public static Account toEntity(AccountRequest request) {
        return Account.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .build();
    }
}
