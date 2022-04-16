package com.moppletop.dsb.domain.dto.item;

import com.moppletop.dsb.config.annotation.Item;
import com.moppletop.dsb.config.annotation.effect.EffectSource;
import com.moppletop.dsb.domain.dto.game.InventorySlot;
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
