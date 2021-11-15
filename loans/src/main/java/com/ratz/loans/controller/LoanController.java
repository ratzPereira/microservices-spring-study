package com.ratz.loans.controller;

import com.ratz.loans.entity.Customer;
import com.ratz.loans.entity.Loans;
import com.ratz.loans.repository.LoansRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoanController {

    @Autowired
    private LoansRepository repository;

    @PostMapping("/myLoans")
    public  List<Loans> getLoansDetails (@RequestBody Customer customer){

        List<Loans> loans = repository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());

        if(loans != null) {
            return loans;
        } else {
            return null;
        }

    }

}
