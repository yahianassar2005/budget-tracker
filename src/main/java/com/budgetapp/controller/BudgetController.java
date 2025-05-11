package com.budgetapp.controller;

import com.budgetapp.dto.BudgetSummaryDTO;
import com.budgetapp.dto.TransactionDTO;
import com.budgetapp.model.Budget;
import com.budgetapp.model.Transaction;
import com.budgetapp.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@CrossOrigin(origins = "*")
public class BudgetController {

    private final BudgetService budgetService;

    @Autowired
    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    // Existing budget endpoints
    @GetMapping
    public List<Budget> getAllBudgets() {
        return budgetService.getAllBudgets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Budget> getBudgetById(@PathVariable Long id) {
        Budget budget = budgetService.getBudgetById(id);
        return ResponseEntity.ok(budget);
    }

    @PostMapping
    public Budget createBudget(@RequestBody Budget budget) {
        return budgetService.createBudget(budget);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable Long id, @RequestBody Budget budget) {
        Budget updated = budgetService.updateBudget(id, budget);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }

    // Transaction endpoints
    @PostMapping("/{budgetId}/transactions")
    public ResponseEntity<Transaction> addTransactionToBudget(
            @PathVariable Long budgetId,
            @RequestParam Long userId,
            @RequestBody TransactionDTO dto) {
        Transaction transaction = budgetService.addTransactionToBudget(budgetId, userId, dto);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/{budgetId}/transactions")
    public ResponseEntity<List<Transaction>> getBudgetTransactions(
            @PathVariable Long budgetId) {
        List<Transaction> transactions = budgetService.getBudgetTransactions(budgetId);
        return ResponseEntity.ok(transactions);
    }

    // Summary endpoints
    @GetMapping("/{id}/summary")
    public ResponseEntity<BudgetSummaryDTO> getBudgetSummary(@PathVariable Long id) {
        BudgetSummaryDTO summary = budgetService.getBudgetSummary(id);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary")
    public ResponseEntity<BudgetSummaryDTO> getOverallBudgetSummary() {
        BudgetSummaryDTO summary = budgetService.getOverallSummary();
        return ResponseEntity.ok(summary);
    }
}