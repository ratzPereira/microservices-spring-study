package com.ratz.accounts.controller;

import com.ratz.accounts.entity.Accounts;
import com.ratz.accounts.entity.Customer;
import com.ratz.accounts.repository.AccountsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountsController {

    @Autowired
    private AccountsRepository repository;

    @PostMapping("/myAccount")
    public Accounts getAccountDetail(@RequestBody Customer customer) {
        Accounts account = repository.findByCustomerId(customer.getCustomerId());

        if(account != null) {
            return account;
        } else {
            return null;
        }
    }
}
