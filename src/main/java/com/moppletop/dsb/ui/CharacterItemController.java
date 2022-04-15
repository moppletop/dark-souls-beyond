package com.moppletop.dsb.ui;

import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.character.PlayerCharacterService;
import com.moppletop.dsb.item.Item;
import com.moppletop.dsb.item.ItemCategory;
import com.moppletop.dsb.item.ItemFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
