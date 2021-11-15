package com.ratz.cards.controller;

import com.ratz.cards.entity.Cards;
import com.ratz.cards.entity.Customer;
import com.ratz.cards.repository.CardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardsController {

    @Autowired
    private CardsRepository repository;

    @PostMapping("myCards")
    public List<Cards> getCardsDetails(@RequestBody Customer customer) {

        List<Cards> cards = repository.findByCustomerId(customer.getCustomerId());

        if(cards != null) {
            return cards;
        } else {
            return null;
        }
    }
}
