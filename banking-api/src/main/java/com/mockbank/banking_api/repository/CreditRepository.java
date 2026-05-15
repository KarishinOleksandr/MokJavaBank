package com.mockbank.banking_api.repository;

import com.mockbank.banking_api.entity.Credit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface CreditRepository extends JpaRepository<Credit, Long> {
    List<Credit> findAllByStartDate(LocalDate startDate);
}