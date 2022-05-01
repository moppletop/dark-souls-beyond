package com.moppletop.dsb.system.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.system.effect.EffectKey;
import com.moppletop.dsb.domain.character.PlayerCharacter;
import com.moppletop.dsb.domain.game.Ability;
import com.moppletop.dsb.domain.game.ActionType;
import com.moppletop.dsb.domain.game.Calculations;
import com.moppletop.dsb.domain.game.RestType;

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
