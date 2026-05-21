package com.haritonov.school.docflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("username", "Секретарь");
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }

    //Заглушки

    @GetMapping("/orders")
    public String orders() {
        return "orders/list";
    }

    @GetMapping("/certificates")
    public String certificates() {
        return "certificates/list";
    }

    @GetMapping("/reports")
    public String reports() {
        return "reports/list";
    }
}
