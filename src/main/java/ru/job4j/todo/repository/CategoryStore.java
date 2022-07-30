package ru.job4j.todo.repository;

import net.snowflake.client.jdbc.internal.net.jcip.annotations.ThreadSafe;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ThreadSafe
@Repository
public class CategoryStore implements Store {
    private final SessionFactory sf;

    public CategoryStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Collection<Category> findAll() {
        return tx(
                session -> {
                    final Query<Category> query = session.createQuery("from Category", Category.class);
                    return query.list();
                },
                sf
        );
    }

    public List<Category> fetchByIds(ArrayList<Long> categoryIds) {
        return tx(session -> session.byMultipleIds(Category.class).multiLoad(categoryIds), sf);
    }
}
