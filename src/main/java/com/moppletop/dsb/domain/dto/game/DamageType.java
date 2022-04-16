package com.moppletop.dsb.domain.dto.game;

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
    RADIENT("Radient"),
    THUNDER("Thunder")
    ;

    private final String name;

}
