package com.mockbank.banking_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data 
@Entity
@Table(name = "credits")
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 

    private Long id;

    private String creditNumber;
    
    private BigDecimal principalAmount; 

    private BigDecimal outstandingAmount;

    private BigDecimal interestRate;

    private LocalDate startDate;

    private LocalDate endDate;
    
    private String borrowerName;

    private String borrowerCode;
}