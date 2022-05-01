package com.moppletop.dsb.controller.ui;

import com.moppletop.dsb.domain.character.PlayerCharacter;
import com.moppletop.dsb.domain.spell.Spell;
import com.moppletop.dsb.domain.spell.SpellFamily;
import com.moppletop.dsb.factory.SpellFactory;
import com.moppletop.dsb.system.player.PlayerCharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
