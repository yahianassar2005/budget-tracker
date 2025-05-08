package com.budgetapp.service;

import com.budgetapp.dto.BudgetSummaryDTO;
import com.budgetapp.model.Budget;
import com.budgetapp.repository.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    private final BudgetRepository budgetRepository;

    @Autowired
    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    // Feature 1: Store user income, expenses, category (already covered)
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Budget getBudgetById(Long id) {
        return budgetRepository.findById(id).orElse(null);
    }

    public Budget createBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    public Budget updateBudget(Long id, Budget updatedBudget) {
        Optional<Budget> optionalBudget = budgetRepository.findById(id);
        if (optionalBudget.isPresent()) {
            Budget existingBudget = optionalBudget.get();
            existingBudget.setIncome(updatedBudget.getIncome());
            existingBudget.setExpenses(updatedBudget.getExpenses());
            existingBudget.setCategory(updatedBudget.getCategory());
            return budgetRepository.save(existingBudget);
        } else {
            return null;
        }
    }

    public boolean deleteBudget(Long id) {
        if (budgetRepository.existsById(id)) {
            budgetRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Feature 2: Calculate total expenses (already handled via getExpenses)

    // Feature 3: Calculate remaining balance
    public int calculateRemaining(Budget budget) {
        return (int) (budget.getIncome() - budget.getExpenses());
    }

    // Feature 4: Suggest cost-cutting based on category
    public String getSuggestion(Budget budget) {
        int remaining = calculateRemaining(budget);
        if (remaining >= 1000) return "You're managing well!";

        String category = budget.getCategory().toLowerCase();
        return switch (category) {
            case "entertainment" -> "Cut back on entertainment spending.";
            case "shopping" -> "Consider reducing your shopping expenses.";
            case "food" -> "Try saving on food costs.";
            default -> "Review your expenses for possible cuts.";
        };
    }

    // Feature 5: Prioritize spending
    public String getPriority(String category) {
        String cat = category.toLowerCase();
        return switch (cat) {
            case "food", "rent", "utilities" -> "Essential";
            default -> "Non-Essential";
        };
    }

    // Feature 6: Financial summary with all calculations
    public BudgetSummaryDTO getBudgetSummary(Budget budget) {
        int remaining = calculateRemaining(budget);
        String suggestion = getSuggestion(budget);
        String priority = getPriority(budget.getCategory());

        return new BudgetSummaryDTO(
                budget.getIncome(),
                budget.getExpenses(),
                remaining,
                suggestion,
                priority
        );
    }
    public BudgetSummaryDTO getOverallSummary() {
        List<Budget> allBudgets = budgetRepository.findAll();

        double totalIncome = allBudgets.stream().mapToDouble(Budget::getIncome).sum();
        double totalExpenses = allBudgets.stream().mapToDouble(Budget::getExpenses).sum();
        double remaining = totalIncome - totalExpenses;

        String suggestion = (remaining < 0) ? "Spending exceeds income." : "You are on track.";
        String priority = (remaining < 0) ? "Critical" : "Balanced";

        return new BudgetSummaryDTO(totalIncome, totalExpenses, remaining, suggestion, priority);
    }

}
