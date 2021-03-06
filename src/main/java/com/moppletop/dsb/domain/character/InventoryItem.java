package com.moppletop.dsb.domain.character;

import com.moppletop.dsb.domain.game.InventorySlot;
import com.moppletop.dsb.system.effect.Item;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@EqualsAndHashCode(of = "id")
public class InventoryItem implements Comparable<InventoryItem> {

    int id;
    Item item;
    int amount;
    InventorySlot slot;

    @Override
    public int compareTo(@NonNull InventoryItem o) {
        if (slot != null && o.slot == null) {
            return -1;
        } else if (slot == null && o.slot != null) {
            return 1;
        }

        return item.getName().compareTo(o.item.getName());
    }
}
