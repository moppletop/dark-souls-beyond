package com.moppletop.dsb.config.annotation.item;

import com.moppletop.dsb.domain.dto.item.EquipmentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ItemCategory {

    CORE("Core Equipment"),

    ARMOUR("Armour", EquipmentType.ARMOUR),

    SMALL_SHIELD("Small Shield", EquipmentType.WEAPON),
    STANDARD_SHIELD("Standard Shield", EquipmentType.WEAPON),
    GREAT_SHIELD("Great Shield", EquipmentType.WEAPON),

    DAGGER("Dagger", EquipmentType.WEAPON),
    STRAIGHT_SWORD("Straight Sword", EquipmentType.WEAPON),
    GREAT_SWORD("Great Sword", EquipmentType.WEAPON),
    CURVED_SWORD("Curved Sword", EquipmentType.WEAPON),
    THRUSTING_SWORD("Thrusting Sword", EquipmentType.WEAPON),
    KATANA("Katana", EquipmentType.WEAPON),
    AXE("Axe", EquipmentType.WEAPON),
    GREAT_AXE("Great Axe", EquipmentType.WEAPON),
    HAMMER("Hammers", EquipmentType.WEAPON),
    GREAT_HAMMER("Great Hammer", EquipmentType.WEAPON),
    SPEAR("Spear and Pike", EquipmentType.WEAPON),
    HALBERD("Halberd", EquipmentType.WEAPON),
    SCYTHE("Scythe", EquipmentType.WEAPON),
    WHIP("Whip", EquipmentType.WEAPON),
    FIST("Fist and Claw", EquipmentType.WEAPON),
    BOW("Bow", EquipmentType.WEAPON),
    GREAT_BOW("Great Bow", EquipmentType.WEAPON),
    CROSS_BOW("Cross Bow", EquipmentType.WEAPON),
    STAVE("Stave", EquipmentType.WEAPON),
    FLAME("Flame", EquipmentType.WEAPON),
    TALISMAN("Talisman", EquipmentType.WEAPON),
    CHIME("Chime", EquipmentType.WEAPON),

    RING("Ring", EquipmentType.RING),
    SOUL("Soul")

    ;

    private final String name;
    private final EquipmentType equipmentType;

    ItemCategory(String name) {
        this(name, null);
    }

    public boolean isBow() {
        return this == BOW || this == GREAT_BOW || this == CROSS_BOW;
    }
}
