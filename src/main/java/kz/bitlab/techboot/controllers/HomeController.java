package kz.bitlab.techboot.controllers;

import kz.bitlab.techboot.entities.Country;
import kz.bitlab.techboot.entities.Item;
import kz.bitlab.techboot.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import kz.bitlab.techboot.repositories.ItemRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class HomeController{

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping(value = "/")
    public String indexPage(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "index";
    }

    @GetMapping(value = "/additem")
    public String addItemPage(Model model){
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
        if(country != null) {
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
    public String detailsPage(@PathVariable(name = "itemId") Long id, Model model){
        Optional<Item> item = itemRepository.findById(id);
        model.addAttribute("item", item.get());
        return "details";
    }

    @GetMapping(value = "/details/update/{itemId}")
    public String updatePage(@PathVariable(name = "itemId") Long id, Model model){
        Optional<Item> item = itemRepository.findById(id);
        model.addAttribute("item", item.get());
        return "update";
    }

    @PostMapping(value = "/details/update")
    public String update(Item item){
        Optional<Item> updateItem = itemRepository.findById(item.getId());
        updateItem.get().setName(item.getName());
        updateItem.get().setDescription(item.getDescription());
        updateItem.get().setPrice(item.getPrice());

        itemRepository.save(updateItem.get());

        return "redirect:/details/" + updateItem.get().getId();
    }

    @PostMapping(value = "/details/delete/{itemId}")
    public String deleteItem(@PathVariable(name="itemId") Long id){
        itemRepository.deleteById(id);
        return "redirect:/";
    }
}