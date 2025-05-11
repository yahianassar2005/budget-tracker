// src/main/java/com/budgetapp/model/Transaction.java
package com.budgetapp.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String category;
    private String description;
    private LocalDate date = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Add this setter
    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;

    // Add this setter:
    public void setBudget(Budget budget) {
        this.budget = budget;
    }
}

