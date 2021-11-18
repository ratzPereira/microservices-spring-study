package com.ratz.loans.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ratz.loans.config.LoansServiceConfig;
import com.ratz.loans.entity.Customer;
import com.ratz.loans.entity.Loans;
import com.ratz.loans.entity.Properties;
import com.ratz.loans.repository.LoansRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class LoanController {

    private static final Logger logger = LoggerFactory.getLogger(LoanController.class);

    @Autowired
    private LoansRepository repository;

    @Autowired
    private LoansServiceConfig loansConfig;

    @PostMapping("/myLoans")
    public List<Loans> getLoansDetails(@RequestHeader("ratzbank-correlation-id") String correlationid, @RequestBody Customer customer) {
        logger.info("getLoansDetails() method started");
        List<Loans> loans = repository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());
        logger.info("getLoansDetails() method ended");
        if (loans != null) {
            return loans;
        } else {
            return null;
        }

    }

    @GetMapping("/loans/properties")
    public String getPropertyDetails() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        Properties properties = new Properties(loansConfig.getMsg(), loansConfig.getBuildVersion(),
                loansConfig.getMailDetails(), loansConfig.getActiveBranches());
        String jsonStr = ow.writeValueAsString(properties);
        return jsonStr;
    }

}
