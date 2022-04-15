package com.moppletop.dsb.item;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.item.jackson.ItemRequirementDeserializer;
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