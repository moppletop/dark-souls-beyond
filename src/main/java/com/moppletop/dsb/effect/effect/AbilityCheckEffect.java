package com.moppletop.dsb.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.game.Ability;
import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.effect.Effect;
import com.moppletop.dsb.effect.EffectKey;
import com.moppletop.dsb.effect.EffectSource;
import com.moppletop.dsb.game.RollType;

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
