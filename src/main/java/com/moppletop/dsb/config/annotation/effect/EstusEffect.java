package com.moppletop.dsb.config.annotation.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.config.annotation.Effect;
import com.moppletop.dsb.config.annotation.EffectKey;
import com.moppletop.dsb.domain.dto.character.PlayerCharacter;
import com.moppletop.dsb.domain.dto.game.ActionType;
import com.moppletop.dsb.domain.dto.game.Calculations;
import com.moppletop.dsb.domain.dto.game.RestType;

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
