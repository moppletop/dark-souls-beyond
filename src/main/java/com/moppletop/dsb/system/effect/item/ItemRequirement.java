package com.moppletop.dsb.system.effect.item;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.moppletop.dsb.system.effect.jackson.ItemRequirementDeserializer;
import com.moppletop.dsb.domain.character.PlayerCharacter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@JsonDeserialize(using = ItemRequirementDeserializer.class)
public abstract class ItemRequirement {

    protected final Map<String, JsonNode> parameters;

    public abstract boolean canEquip(PlayerCharacter playerCharacter);

    public abstract String getDescription();

}
