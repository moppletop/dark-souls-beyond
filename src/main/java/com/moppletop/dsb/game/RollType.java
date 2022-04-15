package com.moppletop.dsb.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RollType {

    ADVANTAGE("Advantage"),
    DISADVANTAGE("Disadvantage");

    private final String name;

}
