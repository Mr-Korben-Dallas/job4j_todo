package ru.job4j.todo.repository;

import net.snowflake.client.jdbc.internal.net.jcip.annotations.ThreadSafe;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Account;

import java.util.Optional;

@ThreadSafe
@Repository
public class AccountStore implements Store {
    private final SessionFactory sf;

    public AccountStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Optional<Account> save(Account account) {
        try {
            Store.super.tx(session -> session.save(account), sf);
        } catch (HibernateException e) {
            return Optional.empty();
        }
        return Optional.of(account);
    }

    public Optional<Account> findAccount(Account account) {
        return Store.super.tx(
                session -> {
                    final Query<Account> query = session.createQuery("from Account where login = :login and password = :password", Account.class);
                    query.setParameter("login", account.getLogin());
                    query.setParameter("password", account.getPassword());
                    return query.uniqueResultOptional();
                },
                sf
        );
    }
}
