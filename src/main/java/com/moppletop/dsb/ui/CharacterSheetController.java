package com.moppletop.dsb.ui;

import com.moppletop.dsb.character.*;
import com.moppletop.dsb.game.Ability;
import com.moppletop.dsb.game.ActionType;
import com.moppletop.dsb.game.Calculations;
import com.moppletop.dsb.spell.Spell;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ui/character/{characterId}")
@RequiredArgsConstructor
class CharacterSheetController {

    private final PlayerCharacterService playerCharacterService;

    @ModelAttribute
    @Transactional
    public void addAttributes(Model model, @PathVariable Integer characterId) {
        PlayerCharacter playerCharacter = playerCharacterService.getCharacter(characterId);

        playerCharacter.assertComplete();

        model.addAttribute("character", playerCharacter);
    }

    @GetMapping
    public String sheet(Model model, @ModelAttribute("character") PlayerCharacter character) {
        model.addAttribute("title", character.getName());

        model.addAttribute("abilityScoreModifiers", getAbilityScoreModifiers(character));
        model.addAttribute("abilityActions", groupAbilityActions(character));
        model.addAttribute("abilityActionUses", abilityActionUses(character));
        model.addAttribute("spellActions", spellsToActions(character));
        model.addAttribute("initiativeSigned", Calculations.getSignedNumber(character.getInitiative()));

        return "sheet/root";
    }

    private Map<Ability, Modification> getAbilityScoreModifiers(PlayerCharacter character) {
        Map<Ability, Modification> abilityScoreModifiers = new TreeMap<>(Comparator.comparingInt(Enum::ordinal));

        character.getAbilityScores().forEach((ability, modification) -> {
            int bonus = Calculations.getAbilityScoreModifier(modification.getMod());
            abilityScoreModifiers.put(ability, new Modification(bonus, modification.getRollType()));
        });

        return abilityScoreModifiers;
    }

    private Map<ActionType, List<AbilityAction>> groupAbilityActions(PlayerCharacter character) {
        return character.getAbilityActions().stream()
                .collect(Collectors.groupingBy(AbilityAction::getActionType, TreeMap::new, Collectors.collectingAndThen(Collectors.toList(), abilityActions -> {
                    abilityActions.sort(null);
                    return abilityActions;
                })));
    }

    private Map<AbilityAction, String> abilityActionUses(PlayerCharacter character) {
        Map<AbilityAction, String> uses = new HashMap<>();
        Map<String, Integer> spentUses = character.getSpentUses();

        for (AbilityAction action : character.getAbilityActions()) {
            if (action.getUses() == null) {
                continue;
            }

            int spent = spentUses.getOrDefault(action.getName(), 0);
            uses.put(action, (action.getUses() - spent) + "/" + action.getUses());
        }

        return uses;
    }

    private List<SpellAction> spellsToActions(PlayerCharacter character) {
        CharacterClass characterClass = character.getCharacterClass();

        if (!characterClass.isSpellCaster()) {
            return Collections.emptyList();
        }

        Ability spellCastingAbility = characterClass.getSpellCaster().getAbility();
        int spellCastingAbilityModifier = Calculations.getAbilityScoreModifier(character.getAbilityScore(spellCastingAbility)) + character.getProficiencyBonus();
        int spellCastingSaveDC = 8 + spellCastingAbilityModifier;

        return character.getLearntSpells().stream()
                .filter(LearntSpell::isAttuned)
                .map(learntSpell -> {
                    Spell spell = learntSpell.getSpell();

                    return new SpellAction(
                            spell.getName(),
                            spell.getCastAction(),
                            spell.getRangeDescription(),
                            spell.getAttackType(),
                            spellCastingAbilityModifier,
                            spellCastingSaveDC,
                            spell.getSavingThrow(),
                            spell.getDescription(),
                            (spell.getCasts() - learntSpell.getSpentCasts()) + "/" + spell.getCasts()
                    );
                })
                .collect(Collectors.toList());
    }

}
