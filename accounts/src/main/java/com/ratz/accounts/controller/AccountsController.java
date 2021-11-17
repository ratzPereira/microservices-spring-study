package com.ratz.accounts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ratz.accounts.config.AccountsServiceConfig;
import com.ratz.accounts.entity.Accounts;
import com.ratz.accounts.entity.Customer;
import com.ratz.accounts.entity.Properties;
import com.ratz.accounts.repository.AccountsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AccountsController {

    @Autowired
    private AccountsRepository repository;

    @Autowired
    private AccountsServiceConfig accountsServiceConfig;

    @PostMapping("/myAccount")
    public Accounts getAccountDetail(@RequestBody Customer customer) {
        Accounts account = repository.findByCustomerId(customer.getCustomerId());

        if (account != null) {
            return account;
        } else {
            return null;
        }
    }


    @GetMapping("/account/properties")
    public String getPropertyDetails() throws JsonProcessingException {

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        Properties properties = new Properties(accountsServiceConfig.getMsg(), accountsServiceConfig.getBuildVersion(),
                accountsServiceConfig.getMailDetails(), accountsServiceConfig.getActiveBranches());

        String jsonStr = ow.writeValueAsString(properties);

        return jsonStr;
    }

}
