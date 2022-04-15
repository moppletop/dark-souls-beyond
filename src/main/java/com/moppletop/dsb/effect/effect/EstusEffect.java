package com.moppletop.dsb.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.game.ActionType;
import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.effect.Effect;
import com.moppletop.dsb.effect.EffectKey;
import com.moppletop.dsb.effect.EffectSource;
import com.moppletop.dsb.game.Calculations;
import com.moppletop.dsb.game.RestType;

import java.util.Map;

@EffectKey("ESTUS")
class EstusEffect extends Effect {

    public EstusEffect(Map<String, JsonNode> parameters) {
        super(parameters);
    }

    @Override
    public void onApply(PlayerCharacter character, EffectSource source) {
        character.addAbility(ActionType.ACTION, source, getDescription(), Calculations.getEstusUses(character.getLevel()));
    }

    @Override
    public boolean onRest(RestType restType) {
        RestType thisRestType = getRestType();

        if (thisRestType == null) {
            return false;
        }

        return restType.includes(thisRestType);
    }
}
