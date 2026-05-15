package com.mockbank.banking_api.controller;

import com.mockbank.banking_api.entity.Credit;
import com.mockbank.banking_api.service.CreditService;
import org.springframework.beans.factory.annotation.Value; // ДОДАНО
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CreditController {

    private final CreditService creditService;

    @Value("${api.security.key}") 
    private String validApiKey;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @GetMapping("/credits")
    public List<Credit> getCredits(
            @RequestHeader(value = "X-Api-Key", required = false) String apiKey,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        if (apiKey == null || !apiKey.equals(validApiKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API Key");
        }

        return creditService.getOrGenerateCreditsForDate(date);
    }
}