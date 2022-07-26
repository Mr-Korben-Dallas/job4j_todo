package ru.job4j.todo.repository;

import net.snowflake.client.jdbc.internal.net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Account;

import java.util.Optional;

@ThreadSafe
@Repository
public class AccountStore {
    private final SessionFactory sf;

    public AccountStore(SessionFactory sf) {
        this.sf = sf;
    }

    public void persist(Account account) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(account);
        session.getTransaction().commit();
        session.close();
    }

    public Optional<Account> findAccount(Account account) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query<Account> query = session.createQuery("from Account where login = :login and password = :password", Account.class);
        query.setParameter("login", account.getLogin());
        query.setParameter("password", account.getPassword());
        Optional<Account> accountDB = query.uniqueResultOptional();
        session.getTransaction().commit();
        session.close();
        return accountDB;
    }
}
