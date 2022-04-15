package com.moppletop.dsb.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.game.ActionType;
import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.game.RestType;
import com.moppletop.dsb.effect.Effect;
import com.moppletop.dsb.effect.EffectKey;
import com.moppletop.dsb.effect.EffectSource;

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
