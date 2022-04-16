package com.moppletop.dsb.config.annotation.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.config.annotation.Effect;
import com.moppletop.dsb.config.annotation.EffectKey;
import com.moppletop.dsb.domain.dto.character.PlayerCharacter;
import com.moppletop.dsb.domain.dto.game.ActionType;
import com.moppletop.dsb.domain.dto.game.RestType;

import java.util.Map;

@EffectKey("ABILITY")
class AbilityEffect extends Effect {

    public AbilityEffect(Map<String, JsonNode> parameters) {
        super(parameters);
    }

    @Override
    public void onApply(PlayerCharacter character, EffectSource source) {
        ActionType action = getEnum("action", ActionType::valueOf);

        if (action == null) {
            action = ActionType.OTHER;
        }

        JsonNode usesNode = parameters.get("uses");
        Integer uses = null;

        if (usesNode != null) {
            uses = usesNode.asInt();
        }

        character.addAbility(action, source, getDescription(), uses);
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
