package com.moppletop.dsb.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.game.ActionType;
import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.effect.Effect;
import com.moppletop.dsb.effect.EffectKey;
import com.moppletop.dsb.effect.EffectSource;
import com.moppletop.dsb.game.RestType;

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
