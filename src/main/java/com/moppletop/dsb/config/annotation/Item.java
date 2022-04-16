package com.moppletop.dsb.config.annotation;

import com.moppletop.dsb.config.annotation.effect.EffectSource;
import com.moppletop.dsb.config.annotation.item.ItemCategory;
import com.moppletop.dsb.config.annotation.item.ItemRequirement;
import com.moppletop.dsb.domain.dto.character.PlayerCharacter;
import com.moppletop.dsb.domain.dto.item.EquipmentType;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value
@EqualsAndHashCode(of = "name")
public class Item implements EffectSource, Comparable<Item> {

    String name;
    String description;
    ItemCategory category;
    Integer cost;
    Boolean hidden = Boolean.FALSE;

    List<ItemRequirement> requirements = Collections.emptyList();
    List<Effect> effects = Collections.emptyList();

    public EquipmentType getEquipmentType() {
        return category.getEquipmentType();
    }

    public boolean canEquip(PlayerCharacter playerCharacter) {
        for (ItemRequirement requirement : requirements) {
            if (!requirement.canEquip(playerCharacter)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int compareTo(Item o) {
        return name.compareTo(o.name);
    }
}
