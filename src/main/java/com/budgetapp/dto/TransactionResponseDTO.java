
// src/main/java/com/budgetapp/dto/TransactionResponseDTO.java
package com.budgetapp.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class TransactionResponseDTO {
    private Long id;
    private Double amount;
    private String category;
    private String description;
    private LocalDate date;
    private String type;
}