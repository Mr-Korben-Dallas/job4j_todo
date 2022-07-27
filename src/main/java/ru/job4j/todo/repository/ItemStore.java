package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.util.Collection;
import java.util.List;

@Repository
public class ItemStore {
    private final TransactionWrapper transactionWrapper;

    public ItemStore(TransactionWrapper transactionWrapper) {
        this.transactionWrapper = transactionWrapper;
    }

    public Collection<Item> findAll() {
        return transactionWrapper.tx(
                session -> {
                    final Query<Item> query = session.createQuery("from Item", Item.class);
                    return query.list();
                }
        );
    }

    public Collection<Item> findByIsDone(boolean isDone) {
        return transactionWrapper.tx(
                session -> {
                    final Query<Item> query = session.createQuery("from Item where is_done = :is_done", Item.class);
                    query.setParameter("is_done", isDone);
                    return query.list();
                }
        );
    }

    public void add(Item item) {
        transactionWrapper.tx(session -> session.save(item));
    }

    public Item findById(Long id) {
        return transactionWrapper.tx(
                session -> {
                    final Query<Item> query = session.createQuery("from Item where id = :id", Item.class);
                    query.setParameter("id", id);
                    return query.uniqueResult();
                }
        );
    }

    public void complete(Long id) {
        transactionWrapper.tx(
                session -> session.createQuery("update Item set is_done = true where id = :id")
                        .setParameter("id", id)
                        .executeUpdate()
        );

    }

    public void update(Item item) {
        transactionWrapper.tx(
                session -> session.createQuery("update Item set name = :name, description = :description where id = :id")
                        .setParameter("name", item.getName())
                        .setParameter("description", item.getDescription())
                        .setParameter("id", item.getId())
                        .executeUpdate()
        );
    }

    public void delete(Long id) {
        transactionWrapper.tx(
                session -> session.createQuery("delete from Item where id = :id")
                        .setParameter("id", id)
                        .executeUpdate()
        );
    }
}
