package com.budgetapp.service;

import com.budgetapp.dto.BudgetSummaryDTO;
import com.budgetapp.dto.TransactionDTO;
import com.budgetapp.dto.TransactionResponseDTO;
import com.budgetapp.model.*;
import com.budgetapp.repository.BudgetRepository;
import com.budgetapp.repository.TransactionRepository;
import com.budgetapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    // Existing Budget CRUD operations
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Budget getBudgetById(Long id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
    }

    public Budget createBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    public Budget updateBudget(Long id, Budget budgetDetails) {
        Budget budget = getBudgetById(id);
        budget.setIncome(budgetDetails.getIncome());
        budget.setExpenses(budgetDetails.getExpenses());
        budget.setCategory(budgetDetails.getCategory());
        return budgetRepository.save(budget);
    }

    public boolean deleteBudget(Long id) {
        if (budgetRepository.existsById(id)) {
            budgetRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Transaction addTransactionToBudget(Long budgetId, Long userId, TransactionDTO dto) {
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new RuntimeException("Budget not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction transaction = new Transaction();
        transaction.setAmount(dto.getAmount());
        transaction.setCategory(dto.getCategory());
        transaction.setDescription(dto.getDescription());
        transaction.setType(TransactionType.valueOf(dto.getType().toUpperCase()));
        transaction.setDate(LocalDate.now());
        transaction.setBudget(budget);
        transaction.setUser(user);  // This will now work

        return transactionRepository.save(transaction);
    }

    public BudgetSummaryDTO getBudgetSummary(Long budgetId) {
        Budget budget = getBudgetById(budgetId);
        List<Transaction> transactions = getBudgetTransactions(budgetId);

        double totalIncome = calculateTotal(transactions, TransactionType.INCOME);
        double totalExpenses = calculateTotal(transactions, TransactionType.EXPENSE);
        double remaining = budget.getIncome() - totalExpenses;

        return new BudgetSummaryDTO(
                budget.getIncome(),
                totalExpenses,
                remaining,
                generateSuggestion(remaining, budget.getIncome()),
                determinePriority(remaining)
        );
    }

    public List<Transaction> getBudgetTransactions(Long budgetId) {
        return transactionRepository.findByBudgetId(budgetId);
    }



    public BudgetSummaryDTO getUserSummary(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        double totalIncome = calculateTotal(transactions, TransactionType.INCOME);
        double totalExpenses = calculateTotal(transactions, TransactionType.EXPENSE);
        double remaining = totalIncome - totalExpenses;

        return new BudgetSummaryDTO(
                totalIncome,
                totalExpenses,
                remaining,
                generateSuggestion(remaining, totalIncome),
                determinePriority(remaining)
        );
    }
    public BudgetSummaryDTO getOverallSummary() {
        List<Budget> budgets = budgetRepository.findAll();
        double totalIncome = budgets.stream().mapToDouble(Budget::getIncome).sum();
        double totalExpenses = budgets.stream().mapToDouble(Budget::getExpenses).sum();
        double remaining = totalIncome - totalExpenses;

        return new BudgetSummaryDTO(
                totalIncome,
                totalExpenses,
                remaining,
                generateSuggestion(remaining, totalIncome),
                determinePriority(remaining)
        );
    }

    // Helper methods
    private double calculateTotal(List<Transaction> transactions, TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    private String generateSuggestion(double remaining, double income) {
        if (remaining <= 0) {
            return "Critical: Expenses exceed income. Immediate action needed.";
        } else if (remaining < income * 0.1) {
            return "Warning: Low remaining balance. Consider reducing expenses.";
        } else if (remaining < income * 0.3) {
            return "Advisory: Moderate remaining balance. Review spending.";
        }
        return "Good: Healthy financial position.";
    }

    private String determinePriority(double remaining) {
        if (remaining <= 0) return "CRITICAL";
        if (remaining < 500) return "HIGH";
        if (remaining < 1000) return "MEDIUM";
        return "LOW";
    }

    // Monthly Budget Tracking
    public BudgetSummaryDTO getMonthlySummary(Long userId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Transaction> transactions = transactionRepository
                .findByUserIdAndDateBetween(userId, start, end);

        double income = calculateTotal(transactions, TransactionType.INCOME);
        double expenses = calculateTotal(transactions, TransactionType.EXPENSE);

        return new BudgetSummaryDTO(
                income,
                expenses,
                income - expenses,
                generateSuggestion(income - expenses, income),
                determinePriority(income - expenses)
        );
    }
}