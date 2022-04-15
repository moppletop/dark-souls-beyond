package com.moppletop.dsb.ui;

import com.moppletop.dsb.character.*;
import com.moppletop.dsb.game.Calculations;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/ui/character/{characterId}/level-up")
@RequiredArgsConstructor
class CharacterLevelUpController {

    private final PlayerCharacterService playerCharacterService;

    @ModelAttribute
    @Transactional
    public void addAttributes(Model model, @PathVariable Integer characterId) {
        PlayerCharacter playerCharacter = playerCharacterService.getCharacter(characterId);

        playerCharacter.assertComplete();

        model.addAttribute("character", playerCharacter);
    }

    @GetMapping
    public String levelUp(Model model, @ModelAttribute("character") PlayerCharacter character) {
        if (character.getLevel() >= 20) {
            throw new PlayerCharacterInvalidException("You cannot level up above 20");
        }

        model.addAttribute("title", "Level Up");
        model.addAttribute("soulsToLevelUp", Calculations.getSoulsForLevel(character.getLevel() + 1));

        return "level-up/root";
    }


}
