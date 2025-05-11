// src/main/java/com/budgetapp/repository/TransactionRepository.java
package com.budgetapp.repository;

import com.budgetapp.model.Transaction;
import com.budgetapp.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Add these methods:
    List<Transaction> findByBudgetId(Long budgetId);
    List<Transaction> findByUserId(Long userId);
    List<Transaction> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);
}