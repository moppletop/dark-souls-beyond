package com.moppletop.dsb.config.annotation.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.config.annotation.Effect;
import com.moppletop.dsb.config.annotation.EffectKey;
import com.moppletop.dsb.domain.dto.character.PlayerCharacter;
import com.moppletop.dsb.domain.dto.game.RollType;
import com.moppletop.dsb.domain.dto.game.Skill;

import java.util.Map;

@EffectKey("SKILL")
public class SkillEffect extends Effect {

    public SkillEffect(Map<String, JsonNode> parameters) {
        super(parameters);
    }

    @Override
    public void onApply(PlayerCharacter character, EffectSource source) {
        Skill skill = getSkill();
        RollType rollType = getRollType();
        JsonNode proficiency = parameters.get("proficiency");

        if (rollType != null) {
            character.modifySavingThrow(source.getName(), skill, rollType);
        }

        if (proficiency != null && proficiency.asBoolean()) {
            character.addProficientSavingThrow(source.getName(), skill);
        }
    }

    @Override
    protected String getDefaultDescription() {
        return "Gain proficiency in " + getSkill().getName() + " skill checks";
    }
}
