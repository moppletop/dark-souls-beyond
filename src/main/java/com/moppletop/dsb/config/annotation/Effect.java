package com.moppletop.dsb.config.annotation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.moppletop.dsb.config.annotation.effect.EffectSource;
import com.moppletop.dsb.config.annotation.jackson.EffectDeserializer;
import com.moppletop.dsb.domain.dto.character.AbilityAction;
import com.moppletop.dsb.domain.dto.character.PlayerCharacter;
import com.moppletop.dsb.domain.dto.character.WeaponAction;
import com.moppletop.dsb.domain.dto.game.Ability;
import com.moppletop.dsb.domain.dto.game.RestType;
import com.moppletop.dsb.domain.dto.game.RollType;
import com.moppletop.dsb.domain.dto.game.Skill;

import java.util.Map;
import java.util.function.Function;

@JsonDeserialize(using = EffectDeserializer.class)
public class Effect {

    protected final Map<String, JsonNode> parameters;

    private String description;

    public Effect(Map<String, JsonNode> parameters) {
        this.parameters = parameters;
    }

    public void onApply(PlayerCharacter character, EffectSource source) {
    }

    public void onPostApply(PlayerCharacter character, EffectSource source) {
    }

    public boolean onRest(RestType restType) {
        return false;
    }

    public void onWeaponAction(PlayerCharacter character, WeaponAction action) {
    }

    public void onAbilityAction(PlayerCharacter character, EffectSource source, AbilityAction action) {
    }

    protected String getDefaultDescription() {
        return "No default description";
    }

    public String getDescription() {
        if (description == null) {
            interpolateDescription();
        }

        return description;
    }

    public Ability getAbility() {
        return getEnum("ability", Ability::valueOf);
    }

    public Skill getSkill() {
        return getEnum("skill", Skill::valueOf);
    }

    public RestType getRestType() {
        return getEnum("rest", RestType::valueOf);
    }

    public RollType getRollType() {
        return getEnum("roll", RollType::valueOf);
    }

    protected <E extends Enum<?>> E getEnum(String parameterKey, Function<String, E> valueOfFunction) {
        JsonNode node = parameters.get(parameterKey);

        if (node == null || !node.isTextual()) {
            return null;
        }

        try {
            return valueOfFunction.apply(node.asText());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void interpolateDescription() {
        if (parameters == null) {
            description = getDefaultDescription();
            return;
        }

        JsonNode descriptionNode = parameters.remove("description");

        if (descriptionNode == null || !descriptionNode.isTextual()) {
            description = getDefaultDescription();
            return;
        }

        description = descriptionNode.asText();
    }

}
