package ru.job4j.todo.controller;

import net.snowflake.client.jdbc.internal.net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.service.ItemService;
import java.util.ArrayList;
import java.util.Collection;

@ThreadSafe
@Controller
public class ItemController {
    private final ItemService service;

    public ItemController(ItemService service) {
        this.service = service;
    }

    @RequestMapping(value = {"/index", "/index/done/{isDone}"})
    public String getViewAll(Model model, @PathVariable(required = false) Boolean isDone) {
        Collection<Item> items = new ArrayList<>();
        if (isDone == null) {
            items = service.findAll();
        }
        if (isDone != null) {
            items = service.findByIsDone(isDone);
        }
        model.addAttribute("items", items);
        return "index";
    }

    @GetMapping("/add")
    public String formCreate(Model model) {
        model.addAttribute("item", new Item());
        return "item/add";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Item item) {
        service.add(item);
        return "redirect:/index";
    }

    @GetMapping("/edit/{itemId}")
    public String editItemForm(Model model, @PathVariable("itemId") Long id) {
        model.addAttribute("item", service.findById(id));
        return "item/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Item item) {
        service.update(item);
        return "redirect:/index";
    }

    @GetMapping("/detail/{id}")
    public String detailForm(Model model, @PathVariable("id") Long id) {

        model.addAttribute("item", service.findById(id));
        return "item/detail";
    }

    @PostMapping("/complete/{id}")
    public String complete(@PathVariable("id") Long id) {
        service.complete(id);
        return "redirect:/index";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        service.delete(id);
        return "redirect:/index";
    }
}
