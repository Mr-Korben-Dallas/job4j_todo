package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class ItemStore {
    private final SessionFactory sf;

    public ItemStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Collection<Item> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Item> items = session.createQuery("from Item", Item.class).list();
        session.getTransaction().commit();
        session.close();
        return items;
    }

    public Collection<Item> findByIsDone(boolean isDone) {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Item> items = session.createQuery("from Item where is_done = :is_done", Item.class)
                .setParameter("is_done", isDone)
                .list();
        session.getTransaction().commit();
        session.close();
        return items;
    }

    public void add(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.persist(item);
        session.getTransaction().commit();
        session.close();
    }

    public Item findById(Long id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query<Item> query = session.createQuery("from Item where id = :id", Item.class);
        query.setParameter("id", id);
        Item item = query.uniqueResult();
        session.getTransaction().commit();
        session.close();
        return item;
    }

    public void complete(Long id) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("update Item set is_done = true where id = :id")
                .setParameter("id", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public void update(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("update Item set name = :name, description = :description where id = :id")
                .setParameter("name", item.getName())
                .setParameter("description", item.getDescription())
                .setParameter("id", item.getId())
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public void delete(Long id) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Item where id = :id")
                .setParameter("id", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
    }
}
