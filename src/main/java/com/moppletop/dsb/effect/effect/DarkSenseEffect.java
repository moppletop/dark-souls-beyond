package com.moppletop.dsb.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.game.Ability;
import com.moppletop.dsb.game.ActionType;
import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.effect.Effect;
import com.moppletop.dsb.effect.EffectKey;
import com.moppletop.dsb.effect.EffectSource;
import com.moppletop.dsb.game.Calculations;
import com.moppletop.dsb.game.RestType;

import java.util.Map;

@EffectKey("DARK_SENSE")
class DarkSenseEffect extends Effect {

    public DarkSenseEffect(Map<String, JsonNode> parameters) {
        super(parameters);
    }

    @Override
    public void onPostApply(PlayerCharacter character, EffectSource source) {
        int uses = Calculations.getAbilityScoreModifier(character.getAbilityScore(Ability.CHARISMA)) + 1;
        character.addAbility(ActionType.ACTION, source, getDescription(), uses);
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
