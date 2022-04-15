package com.moppletop.dsb.item;

import com.moppletop.dsb.game.InventorySlot;
import com.moppletop.dsb.effect.EffectSource;
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
