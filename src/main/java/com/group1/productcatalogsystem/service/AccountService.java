package com.group1.productcatalogsystem.service;

import com.group1.productcatalogsystem.dto.request.AccountRequest;
import com.group1.productcatalogsystem.dto.request.RegisterRequest;
import com.group1.productcatalogsystem.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {

    AccountResponse register(RegisterRequest request);

    AccountResponse createAccount(AccountRequest request);

    AccountResponse getAccountById(Long id);

    List<AccountResponse> getAllAccounts();

    AccountResponse updateAccount(Long id, AccountRequest request);

    void deleteAccount(Long id);
}
