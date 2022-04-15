package com.moppletop.dsb.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.game.Skill;
import com.moppletop.dsb.effect.Effect;
import com.moppletop.dsb.effect.EffectKey;
import com.moppletop.dsb.effect.EffectSource;
import com.moppletop.dsb.game.RollType;

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
