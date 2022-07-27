package ru.job4j.todo.controller;

import net.snowflake.client.jdbc.internal.net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.todo.model.Account;
import ru.job4j.todo.service.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@ThreadSafe
@Controller
public class AccountController {
    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping("/login")
    public String loginPage(Model model, @RequestParam(name = "fail", required = false) Boolean fail, HttpSession session) {
        Account account = service.accountFromSession(session);
        model.addAttribute("account", account);
        model.addAttribute("fail", fail != null);
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Account account, HttpServletRequest req) {
        Optional<Account> accountDB = service.findAccount(account);
        if (accountDB.isEmpty()) {
            return "redirect:/login?fail=true";
        }
        HttpSession session = req.getSession();
        session.setAttribute("account", accountDB.get());
        return "redirect:/index";
    }

    @GetMapping("/signup")
    public String signupPage(Model model, @RequestParam(name = "fail", required = false) Boolean fail, HttpSession session) {
        Account account = service.accountFromSession(session);
        model.addAttribute("account", account);
        model.addAttribute("fail", fail != null);
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute Account account, HttpServletRequest req) {
        Optional<Account> createdAccount = service.save(account);
        if (createdAccount.isEmpty()) {
            return "redirect:/signup?fail=true";
        }
        HttpSession session = req.getSession();
        session.setAttribute("account", account);
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
