package com.moppletop.dsb.config.annotation.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.config.annotation.Effect;
import com.moppletop.dsb.config.annotation.EffectKey;
import com.moppletop.dsb.domain.dto.character.PlayerCharacter;
import com.moppletop.dsb.domain.dto.game.Ability;
import com.moppletop.dsb.domain.dto.game.ActionType;
import com.moppletop.dsb.domain.dto.game.Calculations;
import com.moppletop.dsb.domain.dto.game.RestType;

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
