package ru.job4j.todo.service;

import net.snowflake.client.jdbc.internal.net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.repository.CategoryStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ThreadSafe
@Service
public class CategoryService {
    private final CategoryStore store;

    public CategoryService(CategoryStore store) {
        this.store = store;
    }

    public Collection<Category> findAll() {
        return store.findAll();
    }

    public List<Category> fetchByIds(ArrayList<Long> categoryIds) {
        return store.fetchByIds(categoryIds);
    }
}
