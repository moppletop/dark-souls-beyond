package com.moppletop.dsb.controller.ui;

import com.moppletop.dsb.config.annotation.Item;
import com.moppletop.dsb.config.annotation.item.ItemCategory;
import com.moppletop.dsb.domain.dto.character.PlayerCharacter;
import com.moppletop.dsb.factory.ItemFactory;
import com.moppletop.dsb.service.PlayerCharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/ui/character/{characterId}/item")
@RequiredArgsConstructor
class CharacterItemController {

    private final PlayerCharacterService playerCharacterService;
    private final ItemFactory itemFactory;

    @ModelAttribute
    @Transactional
    public void addAttributes(Model model, @PathVariable Integer characterId) {
        PlayerCharacter playerCharacter = playerCharacterService.getCharacter(characterId);

        playerCharacter.assertComplete();

        model.addAttribute("character", playerCharacter);
    }

    @GetMapping
    public String items(Model model, @ModelAttribute("character") PlayerCharacter character) {
        model.addAttribute("title", "Items");

        model.addAttribute("categories", ItemCategory.values());

        character.getInventory().sort(null);

        return "item/root";
    }

    @GetMapping("/search")
    public String search(
            Model model,
            @RequestParam(required = false) String name,
            @RequestParam(required = false, name = "category") ItemCategory[] categories
    ) {
        Set<ItemCategory> categorySet = new HashSet<>();

        if (categories != null) {
            Collections.addAll(categorySet, categories);
        }

        List<Item> items = itemFactory.search(name, categorySet);

        model.addAttribute("items", items);

        return "item/table";
    }
}
