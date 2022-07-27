package ru.job4j.todo.repository;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.util.Collection;

@Repository
public class ItemStore implements Store {
    private final SessionFactory sf;

    public ItemStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Collection<Item> findAll() {
        return Store.super.tx(
                session -> {
                    final Query<Item> query = session.createQuery("from Item", Item.class);
                    return query.list();
                },
                sf
        );
    }

    public Collection<Item> findByIsDone(boolean isDone) {
        return Store.super.tx(
                session -> {
                    final Query<Item> query = session.createQuery("from Item where is_done = :is_done", Item.class);
                    query.setParameter("is_done", isDone);
                    return query.list();
                },
                sf
        );
    }

    public void add(Item item) {
        Store.super.tx(session -> session.save(item), sf);
    }

    public Item findById(Long id) {
        return Store.super.tx(
                session -> {
                    final Query<Item> query = session.createQuery("from Item where id = :id", Item.class);
                    query.setParameter("id", id);
                    return query.uniqueResult();
                },
                sf
        );
    }

    public void complete(Long id) {
        Store.super.tx(
                session -> session.createQuery("update Item set is_done = true where id = :id")
                        .setParameter("id", id)
                        .executeUpdate(),
                sf
        );

    }

    public void update(Item item) {
        Store.super.tx(
                session -> session.createQuery("update Item set name = :name, description = :description where id = :id")
                        .setParameter("name", item.getName())
                        .setParameter("description", item.getDescription())
                        .setParameter("id", item.getId())
                        .executeUpdate(),
                sf
        );
    }

    public void delete(Long id) {
        Store.super.tx(
                session -> session.createQuery("delete from Item where id = :id")
                        .setParameter("id", id)
                        .executeUpdate(),
                sf
        );
    }
}
