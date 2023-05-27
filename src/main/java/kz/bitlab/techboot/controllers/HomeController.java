package kz.bitlab.techboot.controllers;

import kz.bitlab.techboot.entities.Category;
import kz.bitlab.techboot.entities.Country;
import kz.bitlab.techboot.entities.Item;
import kz.bitlab.techboot.repositories.CategoryRepository;
import kz.bitlab.techboot.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import kz.bitlab.techboot.repositories.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping(value = "/")
    public String indexPage(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "index";
    }

    @GetMapping(value = "/additem")
    public String addItemPage(Model model) {
        List<Country> countries = countryRepository.findAll();
        model.addAttribute("countries", countries);
        return "additem";
    }

    @PostMapping(value = "/additem")
    public String addItem(@RequestParam("name") String name,
                          @RequestParam("description") String description,
                          @RequestParam("price") Double price,
                          @RequestParam("country_id") Long countryId) {
        Optional<Country> country = countryRepository.findById(countryId);
        if (country != null) {
            Item item = new Item();
            item.setName(name);
            item.setDescription(description);
            item.setPrice(price);
            item.setCountry(country.get());
            itemRepository.save(item);
        }
        return "redirect:/";
    }

    @GetMapping(value = "/details/{itemId}")
    public String detailsPage(@PathVariable(name = "itemId") Long id, Model model) {
        Optional<Item> item = itemRepository.findById(id);
        model.addAttribute("item", item.get());
        return "details";
    }

    @GetMapping(value = "/details/update/{itemId}")
    public String updatePage(@PathVariable(name = "itemId") Long id, Model model) {
        Optional<Item> item = itemRepository.findById(id);
        model.addAttribute("item", item.get());

        List<Category> categories = categoryRepository.findAll();
        categories.removeAll(item.get().getCategories());
        model.addAttribute("categories", categories);

        return "update";
    }

    @PostMapping(value = "/details/update")
    public String update(Item item) {
        Optional<Item> updateItem = itemRepository.findById(item.getId());
        updateItem.get().setName(item.getName());
        updateItem.get().setDescription(item.getDescription());
        updateItem.get().setPrice(item.getPrice());

        itemRepository.save(updateItem.get());

        return "redirect:/details/" + updateItem.get().getId();
    }

    @PostMapping(value = "/details/delete/{itemId}")
    public String deleteItem(@PathVariable(name = "itemId") Long id) {
        itemRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping(value = "/assigncategory")
    public String assignCategory(@RequestParam(name = "category_id") Long categoryId,
                                 @RequestParam(name = "item_id") Long carId){
        Item item = itemRepository.findById(carId).orElse(null);
        if(item != null){
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if(category!=null){
                List<Category> categories = item.getCategories();
                if(categories==null) categories = new ArrayList<>();
                categories.add(category);
                item.setCategories(categories);
                itemRepository.save(item);
                return "redirect:/details/" + item.getId();
            }
        }
        return "redirect:/";
    }

    @PostMapping(value = "/unassigncategory")
    public String unassignCategory(@RequestParam(name = "category_id") Long categoryId,
                                   @RequestParam(name = "item_id") Long carId) {
        Item item = itemRepository.findById(carId).orElse(null);
        if (item != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category != null) {
                List<Category> categories = item.getCategories();
                if (categories == null) categories = new ArrayList<>();
                categories.remove(category);
                item.setCategories(categories);
                itemRepository.save(item);
                return "redirect:/details/" + item.getId();
            }
        }
        return "redirect:/";
    }
}