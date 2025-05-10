package com.budgetapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String redirectToLogin() {
        return "redirect:/login";
    }
    @GetMapping("/userDashboard")
    public String index() {
        return "UserDashboard";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/budget")
    public String budgetPage() {
        return "budget";
    }

    @GetMapping("/income-expense")
    public String incomeExpensePage() {
        return "income-expense";
    }

    @GetMapping("/reminder")
    public String reminderPage() {
        return "Reminder";
    }
}
