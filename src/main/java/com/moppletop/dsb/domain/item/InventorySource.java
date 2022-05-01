package com.moppletop.dsb.domain.item;

import com.moppletop.dsb.system.effect.Item;
import com.moppletop.dsb.system.effect.effect.EffectSource;
import com.moppletop.dsb.domain.game.InventorySlot;
import lombok.Value;

@Value
public class InventorySource implements EffectSource {

    Item item;
    InventorySlot slot;

    @Override
    public String getName() {
        return item.getName();
    }
}
