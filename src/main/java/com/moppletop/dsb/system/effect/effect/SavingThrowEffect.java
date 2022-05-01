package com.moppletop.dsb.system.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.system.effect.EffectKey;
import com.moppletop.dsb.domain.character.PlayerCharacter;
import com.moppletop.dsb.domain.game.Ability;
import com.moppletop.dsb.domain.game.RollType;

import java.util.Map;

@EffectKey("SAVING_THROW")
public class SavingThrowEffect extends Effect {

    public SavingThrowEffect(Map<String, JsonNode> parameters) {
        super(parameters);
    }

    @Override
    public void onApply(PlayerCharacter character, EffectSource source) {
        Ability ability = getAbility();
        RollType rollType = getRollType();
        JsonNode proficiency = parameters.get("proficiency");

        if (rollType != null) {
            character.modifySavingThrow(source.getName(), ability, rollType);
        }

        if (proficiency != null && proficiency.asBoolean()) {
            character.addProficientSavingThrow(source.getName(), ability);
        }
    }

    @Override
    protected String getDefaultDescription() {
        JsonNode proficiency = parameters.get("proficiency");

        if (proficiency != null && proficiency.asBoolean()) {
            return "Proficiency in " + getAbility().getName() + " saving throws";
        } else{
            return getRollType().getName() + " on " + getAbility().getName() + " saving throws";
        }
    }
}
