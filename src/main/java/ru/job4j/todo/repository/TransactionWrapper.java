package ru.job4j.todo.repository;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Slf4j
@Component
public class TransactionWrapper {
    private final SessionFactory sf;

    public TransactionWrapper(SessionFactory sf) {
        this.sf = sf;
    }

    public <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            log.error("Transaction error", e);
            throw e;
        } finally {
            session.close();
        }
    }
}
