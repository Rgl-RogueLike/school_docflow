package com.haritonov.school.docflow.controller;

import com.haritonov.school.docflow.modules.user.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final CurrentUserService currentUserService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("username", currentUserService.getCurrentEmployeeFullName());
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

    @GetMapping("/reports")
    public String reports() {
        return "reports/list";
    }
}
