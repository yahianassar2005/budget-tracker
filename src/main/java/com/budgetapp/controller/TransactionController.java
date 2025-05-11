// src/main/java/com/budgetapp/controller/TransactionController.java
package com.budgetapp.controller;

import com.budgetapp.dto.TransactionDTO;
import com.budgetapp.dto.TransactionResponseDTO;
import com.budgetapp.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @RequestParam Long userId,
            @RequestBody TransactionDTO dto) {
        return ResponseEntity.ok(transactionService.createTransaction(userId, dto));
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getUserTransactions(
            @RequestParam Long userId) {
        return ResponseEntity.ok(transactionService.getUserTransactions(userId));
    }
}