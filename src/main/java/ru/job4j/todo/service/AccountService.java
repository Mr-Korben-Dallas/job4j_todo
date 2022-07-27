package ru.job4j.todo.service;

import net.snowflake.client.jdbc.internal.net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Account;
import ru.job4j.todo.repository.AccountStore;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@ThreadSafe
@Service
public class AccountService {
    private final AccountStore store;

    public AccountService(AccountStore store) {
        this.store = store;
    }

    public Optional<Account> save(Account account) {
        return store.save(account);
    }

    public Optional<Account> findAccount(Account account) {
        return store.findAccount(account);
    }

    public Account accountFromSession(HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            account = new Account();
            account.setName("Guest");
        }
        return account;
    }
}
