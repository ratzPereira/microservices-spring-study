package com.ratz.accounts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ratz.accounts.config.AccountsServiceConfig;
import com.ratz.accounts.entity.*;
import com.ratz.accounts.repository.AccountsRepository;

import com.ratz.accounts.service.client.CardsFeignClient;
import com.ratz.accounts.service.client.LoansFeignClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class AccountsController {

    @Autowired
    private AccountsRepository repository;

    @Autowired
    private AccountsServiceConfig accountsServiceConfig;

    @Autowired
    LoansFeignClient loansFeignClient;

    @Autowired
    CardsFeignClient cardsFeignClient;

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

    @PostMapping("/myCustomerDetails")
    //@CircuitBreaker(name = "detailsForCustomerSupportApp",fallbackMethod ="myCustomerDetailsFallBack")
    @Retry(name = "retryForCustomerDetails", fallbackMethod = "myCustomerDetailsFallBack")
    public CustomerDetails myCustomerDetails(@RequestHeader("ratzbank-correlation-id") String correlationid,@RequestBody Customer customer) {

        Accounts accounts = repository.findByCustomerId(customer.getCustomerId());
        List<Loans> loans = loansFeignClient.getLoansDetails(correlationid,customer);
        List<Cards> cards = cardsFeignClient.getCardDetails(correlationid,customer);

        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccounts(accounts);
        customerDetails.setLoans(loans);
        customerDetails.setCards(cards);

        return customerDetails;

    }

    private CustomerDetails myCustomerDetailsFallBack(@RequestHeader("ratzbank-correlation-id") String correlationid,Customer customer, Throwable t) {
        Accounts accounts = repository.findByCustomerId(customer.getCustomerId());
        List<Loans> loans = loansFeignClient.getLoansDetails(correlationid,customer);
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setAccounts(accounts);
        customerDetails.setLoans(loans);
        return customerDetails;

    }

    @GetMapping("/sayHello")
    @RateLimiter(name = "sayHello", fallbackMethod = "sayHelloFallback")
    public String sayHello() {
        return "Hello, Welcome to RatzBank Main";
    }

    private String sayHelloFallback(Throwable t) {
        return "Hi, Welcome to RatzBank";
    }
}
