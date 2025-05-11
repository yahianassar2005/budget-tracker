// src/main/java/com/budgetapp/service/TransactionService.java
package com.budgetapp.service;

import com.budgetapp.dto.TransactionDTO;
import com.budgetapp.dto.TransactionResponseDTO;
import com.budgetapp.model.Transaction;
import com.budgetapp.model.TransactionType;
import com.budgetapp.model.User;
import com.budgetapp.repository.TransactionRepository;
import com.budgetapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionResponseDTO createTransaction(Long userId, TransactionDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setCategory(dto.getCategory());
        transaction.setDescription(dto.getDescription());
        transaction.setType(TransactionType.valueOf(dto.getType()));
        transaction.setUser(user);

        Transaction saved = transactionRepository.save(transaction);
        return convertToDTO(saved);
    }

    public List<TransactionResponseDTO> getUserTransactions(Long userId) {
        return transactionRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TransactionResponseDTO convertToDTO(Transaction t) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(t.getId());
        dto.setAmount(t.getAmount());
        dto.setCategory(t.getCategory());
        dto.setDescription(t.getDescription());
        dto.setDate(t.getDate());
        dto.setType(t.getType().toString());
        return dto;
    }
}