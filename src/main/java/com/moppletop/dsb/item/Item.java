package com.moppletop.dsb.item;

import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.effect.Effect;
import com.moppletop.dsb.effect.EffectSource;
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
