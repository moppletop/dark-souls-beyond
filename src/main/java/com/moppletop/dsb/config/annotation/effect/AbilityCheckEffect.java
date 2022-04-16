package com.moppletop.dsb.config.annotation.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.config.annotation.Effect;
import com.moppletop.dsb.config.annotation.EffectKey;
import com.moppletop.dsb.domain.dto.character.PlayerCharacter;
import com.moppletop.dsb.domain.dto.game.Ability;
import com.moppletop.dsb.domain.dto.game.RollType;

import java.util.Map;

@EffectKey("ABILITY_CHECK")
public class AbilityCheckEffect extends Effect {

    public AbilityCheckEffect(Map<String, JsonNode> parameters) {
        super(parameters);
    }

    @Override
    public void onApply(PlayerCharacter character, EffectSource source) {
        Ability ability = getAbility();
        RollType rollType = getRollType();

        if (rollType != null) {
            character.modifyAbilityScore(source.getName(), ability, rollType);
        }
    }

    @Override
    protected String getDefaultDescription() {
        return getRollType().getName() + " on " + getAbility().getName() + " checks";
    }
}
