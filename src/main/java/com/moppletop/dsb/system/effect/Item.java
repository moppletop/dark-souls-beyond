package com.moppletop.dsb.system.effect;

import com.moppletop.dsb.system.effect.effect.EffectSource;
import com.moppletop.dsb.system.effect.item.ItemCategory;
import com.moppletop.dsb.system.effect.item.ItemRequirement;
import com.moppletop.dsb.domain.character.PlayerCharacter;
import com.moppletop.dsb.domain.item.EquipmentType;
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
