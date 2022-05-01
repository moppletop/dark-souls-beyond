package com.moppletop.dsb.game;

import com.moppletop.dsb.domain.game.Ability;
import com.moppletop.dsb.domain.game.Skill;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SkillTests {

    @Test
    void testSkillsUseRightAbility() {
        // Strength
        assertThat(Skill.ATHLETICS.getAbility()).isEqualTo(Ability.STRENGTH);

        // Dexterity
        assertThat(Skill.ACROBATICS.getAbility()).isEqualTo(Ability.DEXTERITY);
        assertThat(Skill.SLEIGHT_OF_HAND.getAbility()).isEqualTo(Ability.DEXTERITY);
        assertThat(Skill.STEALTH.getAbility()).isEqualTo(Ability.DEXTERITY);

        // Intelligence
        assertThat(Skill.ARCANA.getAbility()).isEqualTo(Ability.INTELLIGENCE);
        assertThat(Skill.HISTORY.getAbility()).isEqualTo(Ability.INTELLIGENCE);
        assertThat(Skill.INVESTIGATION.getAbility()).isEqualTo(Ability.INTELLIGENCE);
        assertThat(Skill.NATURE.getAbility()).isEqualTo(Ability.INTELLIGENCE);
        assertThat(Skill.RELIGION.getAbility()).isEqualTo(Ability.INTELLIGENCE);

        // Wisdom
        assertThat(Skill.ANIMAL_HANDLING.getAbility()).isEqualTo(Ability.WISDOM);
        assertThat(Skill.INSIGHT.getAbility()).isEqualTo(Ability.WISDOM);
        assertThat(Skill.MEDICINE.getAbility()).isEqualTo(Ability.WISDOM);
        assertThat(Skill.PERCEPTION.getAbility()).isEqualTo(Ability.WISDOM);
        assertThat(Skill.SURVIVAL.getAbility()).isEqualTo(Ability.WISDOM);

        // Charisma
        assertThat(Skill.DECEPTION.getAbility()).isEqualTo(Ability.CHARISMA);
        assertThat(Skill.INTIMIDATION.getAbility()).isEqualTo(Ability.CHARISMA);
        assertThat(Skill.PERFORMANCE.getAbility()).isEqualTo(Ability.CHARISMA);
        assertThat(Skill.PERSUASION.getAbility()).isEqualTo(Ability.CHARISMA);
    }

}
