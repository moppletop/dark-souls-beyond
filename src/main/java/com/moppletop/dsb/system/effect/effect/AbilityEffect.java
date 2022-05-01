package com.moppletop.dsb.system.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.system.effect.EffectKey;
import com.moppletop.dsb.domain.character.PlayerCharacter;
import com.moppletop.dsb.domain.game.ActionType;
import com.moppletop.dsb.domain.game.RestType;

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
