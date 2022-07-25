package ru.job4j.todo.service;

import net.snowflake.client.jdbc.internal.net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.repository.ItemStore;

import java.util.Collection;

@ThreadSafe
@Service
public class ItemService {
    private final ItemStore store;

    public ItemService(ItemStore store) {
        this.store = store;
    }

    public Collection<Item> findAll() {
        return store.findAll();
    }

    public Collection<Item> findByIsDone(boolean isDone) {
        return store.findByIsDone(isDone);
    }

    public void add(Item item) {
        store.add(item);
    }

    public Item findById(Long id) {
        return store.findById(id);
    }

    public void complete(Long id) {
        store.complete(id);
    }

    public void update(Item item) {
        store.update(item);
    }

    public void delete(Long id) {
        store.delete(id);
    }
}
