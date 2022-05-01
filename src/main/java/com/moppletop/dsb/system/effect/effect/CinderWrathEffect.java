package com.moppletop.dsb.system.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.system.effect.EffectKey;
import com.moppletop.dsb.domain.character.PlayerCharacter;
import com.moppletop.dsb.domain.game.ActionType;
import com.moppletop.dsb.domain.game.RestType;

import java.util.Map;

@EffectKey("CINDER_WRATH")
class CinderWrathEffect extends Effect {

    public CinderWrathEffect(Map<String, JsonNode> parameters) {
        super(parameters);
    }

    @Override
    public void onApply(PlayerCharacter character, EffectSource source) {
        int uses = character.getCharacterClass().getSpecialSlots().getLevels().get(character.getLevel());
        character.addAbility(ActionType.BONUS, source, getDescription(), uses);
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
