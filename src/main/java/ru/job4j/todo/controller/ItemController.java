package ru.job4j.todo.controller;

import net.snowflake.client.jdbc.internal.net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Account;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.service.AccountService;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.ItemService;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ThreadSafe
@Controller
public class ItemController {
    private final ItemService service;
    private final AccountService accountService;
    private final CategoryService categoryService;

    public ItemController(ItemService service, AccountService accountService, CategoryService categoryService) {
        this.service = service;
        this.accountService = accountService;
        this.categoryService = categoryService;
    }

    @RequestMapping(value = {"/index", "/index/done/{isDone}"})
    public String getViewAll(Model model, @PathVariable(required = false) Boolean isDone, HttpSession session) {
        Collection<Item> items = new ArrayList<>();
        if (isDone == null) {
            items = service.findAll();
        }
        if (isDone != null) {
            items = service.findByIsDone(isDone);
        }
        Account account = accountService.accountFromSession(session);
        model.addAttribute("account", account);
        model.addAttribute("items", items);
        return "index";
    }

    @GetMapping("/add")
    public String formCreate(Model model, HttpSession session) {
        Account account = accountService.accountFromSession(session);
        model.addAttribute("account", account);
        model.addAttribute("item", new Item());
        model.addAttribute("categories", categoryService.findAll());
        return "item/add";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Item item, @RequestParam(name = "categoryIds") ArrayList<Long> categoryIds, HttpSession session) {
        Account account = accountService.accountFromSession(session);
        item.setAccount(account);
        List<Category> categories = categoryService.fetchByIds(categoryIds);
        if (!categories.isEmpty()) {
            item.addCategories(categories);
        }
        service.add(item);
        return "redirect:/index";
    }

    @GetMapping("/edit/{itemId}")
    public String editItemForm(Model model, @PathVariable("itemId") Long id, HttpSession session) {
        Account account = accountService.accountFromSession(session);
        model.addAttribute("account", account);
        model.addAttribute("item", service.findById(id));
        return "item/edit";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Item item) {
        service.update(item);
        return "redirect:/index";
    }

    @GetMapping("/detail/{id}")
    public String detailForm(Model model, @PathVariable("id") Long id, HttpSession session) {
        Account account = accountService.accountFromSession(session);
        model.addAttribute("account", account);
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
