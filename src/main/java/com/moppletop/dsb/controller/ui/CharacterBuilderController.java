package com.moppletop.dsb.controller.ui;

import com.moppletop.dsb.system.effect.effect.SavingThrowEffect;
import com.moppletop.dsb.system.effect.effect.SkillEffect;
import com.moppletop.dsb.domain.ClassFeatureTable;
import com.moppletop.dsb.domain.character.CharacterClass;
import com.moppletop.dsb.domain.character.CharacterClassFactory;
import com.moppletop.dsb.domain.character.CharacterClassFeature;
import com.moppletop.dsb.domain.character.Origin;
import com.moppletop.dsb.domain.character.PlayerCharacter;
import com.moppletop.dsb.domain.game.Calculations;
import com.moppletop.dsb.factory.OriginFactory;
import com.moppletop.dsb.system.player.PlayerCharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ui/character/{characterId}/builder")
@RequiredArgsConstructor
class CharacterBuilderController {

    private final PlayerCharacterService playerCharacterService;
    private final OriginFactory originFactory;
    private final CharacterClassFactory classFactory;

    @ModelAttribute
    @Transactional
    public void addAttributes(Model model, @PathVariable Integer characterId) {
        model.addAttribute("character", playerCharacterService.getCharacter(characterId));
    }

    @GetMapping("/{step}")
    public String builder(Model model, @PathVariable String step, @ModelAttribute("character") PlayerCharacter character) {
        String title;

        switch (step) {
            case "character":
                title = "Character Information";
                break;
            case "origin":
                title = "Origin";
                model.addAttribute("origins", originFactory.getAll());
                break;
            case "class":
                title = "Class";
                model.addAttribute("classes", classFactory.getAll());
                break;
            case "class-features":
                character.assertComplete();

                title = "Class Features";
                List<Map.Entry<Integer, List<CharacterClassFeature>>> unlockedFeatures = character.getCharacterClass().getLevelFeatures().entrySet().stream()
                        .filter(entry -> entry.getKey() <= character.getLevel())
                        .sorted(Map.Entry.comparingByKey())
                        .collect(Collectors.toList());
                model.addAttribute("unlockedFeatures", unlockedFeatures);
                break;
            default:
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        model.addAttribute("title", title);
        model.addAttribute("fragment", step);

        return "builder/root";
    }

    @GetMapping("/origin/{originId}")
    public String originInfo(Model model, @PathVariable Integer characterId, @PathVariable Integer originId) {
        Origin origin = originFactory.getById(originId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("title", origin.getName());
        model.addAttribute("fragment", "origin-show-more");
        model.addAttribute("origin", origin);
        model.addAttribute("characterId", characterId);

        return "builder/root";
    }

    @GetMapping("/class/{classId}")
    public String classInfo(Model model, @PathVariable Integer characterId, @PathVariable Integer classId) {
        CharacterClass characterClass = classFactory.getById(classId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        model.addAttribute("title", characterClass.getName());
        model.addAttribute("fragment", "class-show-more");
        model.addAttribute("class", characterClass);
        model.addAttribute("characterId", characterId);

        model.addAttribute("savingThrowList", getProficientSavingThrows(characterClass));
        model.addAttribute("skillList", getProficientSkills(characterClass));
        model.addAttribute("classFeatureTable", getClassFeatureTable(characterClass));

        return "builder/root";
    }

    private String getProficientSavingThrows(CharacterClass characterClass) {
        List<CharacterClassFeature> features = characterClass.getLevelFeatures().get(1); // Class proficiencies given at level 1

        for (CharacterClassFeature feature : features) {
            if (!feature.getName().endsWith("Saving Throw Proficiencies")) {
                continue;
            }

            if (feature.getOptions().isEmpty()) {
                return feature.getEffects().stream()
                        .filter(effect -> effect instanceof SavingThrowEffect)
                        .map(effect -> effect.getAbility().getName())
                        .collect(Collectors.joining(", "));
            } else {
                return "Choose any two abilities from: " + feature.getOptions().stream()
                        .map(CharacterClassFeature::getEffects)
                        .flatMap(Collection::stream)
                        .filter(effect -> effect instanceof SavingThrowEffect)
                        .map(effect -> effect.getAbility().getName())
                        .collect(Collectors.joining(", "));
            }
        }

        return null;
    }

    private String getProficientSkills(CharacterClass characterClass) {
        List<CharacterClassFeature> features = characterClass.getLevelFeatures().get(1); // Class skills given at level 1

        for (CharacterClassFeature feature : features) {
            if (!feature.getName().endsWith("Skill Proficiencies")) {
                continue;
            }

            if (feature.getOptions().isEmpty()) {
                return feature.getEffects().stream()
                        .filter(effect -> effect instanceof SkillEffect)
                        .map(effect -> effect.getSkill().getName())
                        .collect(Collectors.joining(", "));
            } else {
                return "Choose any two skills from: " + feature.getOptions().stream()
                        .map(CharacterClassFeature::getEffects)
                        .flatMap(Collection::stream)
                        .filter(effect -> effect instanceof SkillEffect)
                        .map(effect -> effect.getSkill().getName())
                        .collect(Collectors.joining(", "));
            }
        }

        return null;
    }

    private List<ClassFeatureTable> getClassFeatureTable(CharacterClass characterClass) {
        List<ClassFeatureTable> table = new ArrayList<>(20);
        Map<Integer, List<CharacterClassFeature>> levelEffects = characterClass.getLevelFeatures();

        for (int i = 1; i <= 20; i++) {
            List<CharacterClassFeature> features = levelEffects.get(i);
            int proficiencyBonus = Calculations.getProficiencyBonus(i);
            String featuresJoined = features == null || features.isEmpty() ? "-" : features.stream().map(CharacterClassFeature::getName).collect(Collectors.joining(", "));

            table.add(new ClassFeatureTable(i, proficiencyBonus, featuresJoined));
        }

        return table;
    }

}
