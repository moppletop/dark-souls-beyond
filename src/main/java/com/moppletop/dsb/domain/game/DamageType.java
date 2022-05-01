package com.moppletop.dsb.domain.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DamageType {

    ACID("Acid"),
    BLUDGEONING("Bludgeoning"),
    COLD("Cold"),
    FIRE("Fire"),
    FORCE("Force"),
    SLASHING("Slashing"),
    LIGHTNING("Lightning"),
    NECROTIC("Necrotic"),
    PIERCING("Piercing"),
    POISON("Poison"),
    RADIANT("Radiant"),
    THUNDER("Thunder")
    ;

    private final String name;

}
