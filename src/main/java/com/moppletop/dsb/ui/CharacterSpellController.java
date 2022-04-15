package com.moppletop.dsb.ui;

import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.character.PlayerCharacterService;
import com.moppletop.dsb.spell.Spell;
import com.moppletop.dsb.spell.SpellFactory;
import com.moppletop.dsb.spell.SpellFamily;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("/ui/character/{characterId}/spell")
@RequiredArgsConstructor
class CharacterSpellController {

    private final PlayerCharacterService playerCharacterService;
    private final SpellFactory spellFactory;

    @ModelAttribute
    @Transactional
    public void addAttributes(Model model, @PathVariable Integer characterId) {
        PlayerCharacter playerCharacter = playerCharacterService.getCharacter(characterId);

        playerCharacter.assertComplete();

        model.addAttribute("character", playerCharacter);
    }

    @GetMapping
    public String spells(Model model, @ModelAttribute("character") PlayerCharacter character) {
        model.addAttribute("title", "Spells");
        model.addAttribute("families", SpellFamily.values());
        model.addAttribute("attunementPercentage", ((double) character.getUsedAttunementSlots() / character.getMaxAttunementSlots()) * 100);

        character.getLearntSpells().sort(null);

        return "spell/root";
    }

    @GetMapping("/search")
    public String search(
            Model model,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) SpellFamily family) {
        List<Spell> spells = spellFactory.search(name, family);

        model.addAttribute("spells", spells);

        return "spell/table";
    }


}
