// src/main/java/com/budgetapp/dto/TransactionDTO.java
package com.budgetapp.dto;

import lombok.Data;

@Data
public class TransactionDTO {
    private Double amount;
    private String category;
    private String description;
    private String type; // "INCOME" or "EXPENSE"
}
