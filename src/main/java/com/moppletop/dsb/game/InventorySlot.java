package com.moppletop.dsb.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum InventorySlot {

    ARMOUR("Armour"),
    RIGHT_HAND_WEAPON("Right Hand Weapon"),
    LEFT_HAND_WEAPON("Left Hand Weapon/Shield/Two-Handed"),
    RIGHT_HAND_RING("Right Hand Ring"),
    LEFT_HAND_RING("Left Hand Ring");

    private final String name;

    public boolean isTwoHanded() {
        return this == LEFT_HAND_WEAPON;
    }

}
