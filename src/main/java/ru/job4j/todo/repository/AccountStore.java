package ru.job4j.todo.repository;

import net.snowflake.client.jdbc.internal.net.jcip.annotations.ThreadSafe;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Account;

import java.util.Optional;

@ThreadSafe
@Repository
public class AccountStore {
    private final TransactionWrapper transactionWrapper;

    public AccountStore(TransactionWrapper transactionWrapper) {
        this.transactionWrapper = transactionWrapper;
    }

    public void persist(Account account) {
        transactionWrapper.tx(session -> session.save(account));
    }

    public Optional<Account> findAccount(Account account) {
        return transactionWrapper.tx(
                session -> {
                    final Query<Account> query = session.createQuery("from Account where login = :login and password = :password", Account.class);
                    query.setParameter("login", account.getLogin());
                    query.setParameter("password", account.getPassword());
                    return query.uniqueResultOptional();
                }
        );
    }
}
