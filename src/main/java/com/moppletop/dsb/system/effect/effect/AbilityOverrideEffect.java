package com.moppletop.dsb.system.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.system.effect.EffectKey;
import com.moppletop.dsb.domain.character.AbilityAction;
import com.moppletop.dsb.domain.character.PlayerCharacter;

import java.util.Map;

@EffectKey("ABILITY_OVERRIDE")
class AbilityOverrideEffect extends Effect {

    private final String ability;

    private String description;
    private Integer uses;

    public AbilityOverrideEffect(Map<String, JsonNode> parameters) {
        super(parameters);

        this.ability = parameters.get("ability").asText();

        JsonNode descriptionNode = parameters.get("description");

        if (descriptionNode != null) {
            this.description = descriptionNode.asText();
        }

        JsonNode usesNode = parameters.get("uses");

        if (usesNode != null) {
            this.uses = usesNode.asInt();
        }
    }

    @Override
    public void onAbilityAction(PlayerCharacter character, EffectSource source, AbilityAction action) {
        if (!action.getName().equals(ability)) {
            return;
        }

        action.setName(source.getName());

        if (description != null) {
            action.setDescription(description);
        }

        if (uses != null) {
            action.setUses(uses);
        }
    }
}
