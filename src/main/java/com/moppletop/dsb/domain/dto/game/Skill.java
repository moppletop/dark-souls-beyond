package com.moppletop.dsb.domain.dto.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Skill {

    ACROBATICS("Acrobatics", Ability.DEXTERITY),
    ANIMAL_HANDLING("Animal Handling", Ability.WISDOM),
    ARCANA("Arcana", Ability.INTELLIGENCE),
    ATHLETICS("Athletics", Ability.STRENGTH),
    DECEPTION("Deception", Ability.CHARISMA),
    HISTORY("History", Ability.INTELLIGENCE),
    INSIGHT("Insight", Ability.WISDOM),
    INTIMIDATION("Intimation", Ability.CHARISMA),
    INVESTIGATION("Investigation", Ability.INTELLIGENCE),
    MEDICINE("Medicine", Ability.WISDOM),
    NATURE("Nature", Ability.INTELLIGENCE),
    PERCEPTION("Perception", Ability.WISDOM),
    PERFORMANCE("Performance", Ability.CHARISMA),
    PERSUASION("Persuasion", Ability.CHARISMA),
    RELIGION("Religion", Ability.INTELLIGENCE),
    SLEIGHT_OF_HAND("Sleight of hand", Ability.DEXTERITY),
    STEALTH("Stealth", Ability.DEXTERITY),
    SURVIVAL("Survival", Ability.WISDOM);

    private final String name;
    private final Ability ability;


}
