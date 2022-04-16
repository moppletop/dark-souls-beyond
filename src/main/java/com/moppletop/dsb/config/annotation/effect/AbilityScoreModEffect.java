package com.moppletop.dsb.config.annotation.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.config.annotation.Effect;
import com.moppletop.dsb.config.annotation.EffectKey;
import com.moppletop.dsb.domain.dto.character.PlayerCharacter;

import java.util.Map;

@EffectKey("ABILITY_SCORE")
public class AbilityScoreModEffect extends Effect {

    public AbilityScoreModEffect(Map<String, JsonNode> parameters) {
        super(parameters);
    }

    @Override
    public void onApply(PlayerCharacter character, EffectSource source) {
        character.modifyAbilityScore(source.getName(), getAbility(), parameters.get("mod").asInt());
    }

    @Override
    protected String getDefaultDescription() {
        int mod = parameters.get("mod").asInt();

        if (mod > 0) {
            return "Increase your " + getAbility().getName() + " by " + mod;
        } else {
            return "Decrease your " + getAbility().getName() + " by " + -mod;
        }
    }
}
