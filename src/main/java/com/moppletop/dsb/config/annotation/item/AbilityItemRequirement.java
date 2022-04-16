package com.moppletop.dsb.config.annotation.item;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.config.annotation.ItemRequirementKey;
import com.moppletop.dsb.domain.dto.character.PlayerCharacter;
import com.moppletop.dsb.domain.dto.game.Ability;

import java.util.Map;

@ItemRequirementKey("ABILITY")
class AbilityItemRequirement extends ItemRequirement {

    private final Ability ability;
    private final int requirement;

    public AbilityItemRequirement(Map<String, JsonNode> parameters) {
        super(parameters);

        ability = Ability.valueOf(parameters.get("ability").asText());
        requirement = parameters.get("score").asInt();
    }

    @Override
    public boolean canEquip(PlayerCharacter playerCharacter) {
        return playerCharacter.getAbilityScore(ability) >= requirement;
    }

    @Override
    public String getDescription() {
        return requirement + " " + ability.getName();
    }
}
