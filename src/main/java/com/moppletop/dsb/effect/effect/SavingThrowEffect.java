package com.moppletop.dsb.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.game.Ability;
import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.effect.Effect;
import com.moppletop.dsb.effect.EffectKey;
import com.moppletop.dsb.effect.EffectSource;
import com.moppletop.dsb.game.RollType;

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
