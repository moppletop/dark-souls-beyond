package com.moppletop.dsb.domain.dto.character;

import com.moppletop.dsb.config.annotation.Item;
import com.moppletop.dsb.domain.dto.game.InventorySlot;
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
