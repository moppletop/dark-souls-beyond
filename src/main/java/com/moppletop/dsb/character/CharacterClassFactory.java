package com.moppletop.dsb.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moppletop.dsb.effect.effect.AbilityScoreImprovementEffect;
import com.moppletop.dsb.game.Ability;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CharacterClassFactory {

    private final ObjectMapper objectMapper;
    private final CommonCharacterFeatureOptions commonCharacterFeatureOptions;

    private final Map<Integer, CharacterClass> classesById = new HashMap<>();
    private final Map<String, CharacterClassFeature> featuresByName = new HashMap<>();

    @PostConstruct
    void generateItems() throws IOException {
        File dir = new File("source", "classes");

        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".json"));

        if (files == null) {
            return;
        }

        Map<Integer, List<CharacterClassFeature>> abilityScoreImprovements = generateAbilityScoreImprovements();

        for (List<CharacterClassFeature> features : abilityScoreImprovements.values()) {
            for (CharacterClassFeature feature : features) {
                featuresByName.put(feature.getName(), feature);
            }
        }

        for (File file : files) {
            CharacterClass characterClass = objectMapper.readValue(file, CharacterClass.class);

            characterClass.getLevelFeatures().putAll(abilityScoreImprovements);

            classesById.put(characterClass.getId(), characterClass);

            for (List<CharacterClassFeature> features : characterClass.getLevelFeatures().values()) {
                for (CharacterClassFeature feature : features) {
                    for (String commonOption : feature.getCommonOptions()) {
                        if (feature.getOptions().isEmpty()) { // Collections.emptyList used
                            feature.setOptions(new ArrayList<>());
                        }

                        commonCharacterFeatureOptions.getByName(commonOption).ifPresent(option -> feature.getOptions().add(option));
                    }

                    feature.getCommonOptions().clear();
                    featuresByName.put(feature.getName(), feature);
                }
            }
        }

        log.info("Loaded {} classes", classesById.size());
    }

    private Map<Integer, List<CharacterClassFeature>> generateAbilityScoreImprovements() {
        List<CharacterClassFeature> options = new ArrayList<>(Ability.values().length);

        for (Ability ability : Ability.values()) {
            CharacterClassFeature feature = new CharacterClassFeature(ability.getName());

            feature.setEffects(Collections.singletonList(new AbilityScoreImprovementEffect(ability)));

            options.add(feature);
        }

        Map<Integer, List<CharacterClassFeature>> featuresByLevel = new HashMap<>();

        for (int level : CharacterClass.ABILITY_IMPROVEMENT_LEVELS) {
            List<CharacterClassFeature> features = new ArrayList<>(2);

            for (int i = 1; i <= 2; i++) {
                CharacterClassFeature feature = new CharacterClassFeature("Ability Score Improvement " + level + "-" + i);

                feature.setAllowedOptions(1);
                feature.setOptions(options);

                features.add(feature);
            }

            featuresByLevel.put(level, features);
        }

        return featuresByLevel;
    }

    public Optional<CharacterClass> getById(int classId) {
        return Optional.ofNullable(classesById.get(classId));
    }

    public Optional<CharacterClassFeature> getFeatureByName(String featureName) {
        return Optional.ofNullable(featuresByName.get(featureName));
    }

    public Optional<CharacterClassFeature> getFeatureOptionByName(String featureName, String optionName) {
        CharacterClassFeature feature = featuresByName.get(featureName);

        if (feature == null) {
            return Optional.empty();
        }

        for (CharacterClassFeature option : feature.getOptions()) {
            if (option.getName().equals(optionName)) {
                return Optional.of(option);
            }
        }

        return Optional.empty();
    }

    public List<CharacterClass> getAll() {
        return classesById.values().stream()
                .sorted()
                .collect(Collectors.toList());
    }

}
