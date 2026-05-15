package com.mockbank.banking_api.service;

import com.mockbank.banking_api.entity.Credit;
import com.mockbank.banking_api.repository.CreditRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CreditService {

    private final CreditRepository repository;
    private final Random random = new Random();

    public CreditService(CreditRepository repository) {
        this.repository = repository;
    }

    public List<Credit> getOrGenerateCreditsForDate(LocalDate date) {
        List<Credit> existingCredits = repository.findAllByStartDate(date);
        if (!existingCredits.isEmpty()) {
            boolean wasUpdated = false;

            for (Credit credit : existingCredits) {
                if (credit.getOutstandingAmount().compareTo(BigDecimal.ZERO) > 0 && random.nextDouble() < 0.30) {
                    
                    double paymentPercent = 0.01 + (0.09 * random.nextDouble());
                    BigDecimal paymentAmount = credit.getPrincipalAmount().multiply(BigDecimal.valueOf(paymentPercent));
                    
                    BigDecimal newOutstanding = credit.getOutstandingAmount().subtract(paymentAmount);
                    
                    if (newOutstanding.compareTo(BigDecimal.ZERO) < 0) {
                        newOutstanding = BigDecimal.ZERO;
                    }
                    
                    credit.setOutstandingAmount(newOutstanding.setScale(2, RoundingMode.HALF_UP));
                    wasUpdated = true;
                }
            }

            if (wasUpdated) {
                repository.saveAll(existingCredits);
            }

            return existingCredits; 
        }

        List<Credit> newCredits = new ArrayList<>();
        int count = random.nextInt(5) + 5; 

        for (int i = 0; i < count; i++) {
            Credit c = new Credit();
            
            c.setCreditNumber("CR-OVD-" + (10000 + random.nextInt(90000)));
            
            double amount = 10000 + (140000) * random.nextDouble();
            c.setPrincipalAmount(BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP));
            
            c.setOutstandingAmount(c.getPrincipalAmount()); 
            
            c.setInterestRate(BigDecimal.valueOf(0.15 + 0.20 * random.nextDouble()).setScale(4, RoundingMode.HALF_UP));
            
            c.setStartDate(date);
            c.setEndDate(date.plusMonths(12 + random.nextInt(24))); 

            if (random.nextBoolean()) {
                c.setBorrowerCode(String.format("%08d", random.nextInt(99999999)));
                String[] names = {"ТОВ 'Агро Інвест'", "ПП 'Буд-Гарант'", "ТОВ 'Еко Логістика'", "ПрАТ 'ТехноМаш'"};
                c.setBorrowerName(names[random.nextInt(names.length)]);
            } else {
                long code = 1000000000L + (long)(random.nextDouble() * 8999999999L);
                c.setBorrowerCode(String.valueOf(code));
                String[] names = {"Коваленко Іван Петрович", "Шевченко Ганна Олегівна", "Бойко Тарас Андрійович", "Мельник Олена Василівна"};
                c.setBorrowerName(names[random.nextInt(names.length)]);
            }

            newCredits.add(c);
        }

        return repository.saveAll(newCredits);
    }
}