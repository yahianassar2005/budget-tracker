package com.budgetapp.dto;

public class BudgetSummaryDTO {
    private double income;
    private double expenses;
    private double remaining;
    private String suggestion;
    private String priority;

    public BudgetSummaryDTO(double income, double expenses, double remaining, String suggestion, String priority) {
        this.income = income;
        this.expenses = expenses;
        this.remaining = remaining;
        this.suggestion = suggestion;
        this.priority = priority;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    public double getRemaining() {
        return remaining;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
