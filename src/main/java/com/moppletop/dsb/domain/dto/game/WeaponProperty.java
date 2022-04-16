package com.moppletop.dsb.domain.dto.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WeaponProperty {

    AMMUNITION("Ammunition"),
    THROWN("Thrown"),
    VERSATILE("Versatile"),
    FINESSE("Finesse"),
    LIGHT("Light"),
    LOADING("Loading"),
    REACH("Reach"),
    RANGE("Range"),
    TWO_HANDED("Two-Handed")
    ;

    private final String name;

}
