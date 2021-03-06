package com.moppletop.dsb.domain.item;

import com.moppletop.dsb.domain.game.InventorySlot;
import lombok.Getter;

@Getter
public enum EquipmentType {

    ARMOUR(InventorySlot.ARMOUR),
    WEAPON(InventorySlot.LEFT_HAND_WEAPON, InventorySlot.RIGHT_HAND_WEAPON),
    RING(InventorySlot.LEFT_HAND_RING, InventorySlot.RIGHT_HAND_RING),;

    private final InventorySlot[] slots;

    EquipmentType(InventorySlot... slots) {
        this.slots = slots;
    }
}
