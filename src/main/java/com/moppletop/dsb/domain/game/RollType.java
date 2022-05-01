package com.moppletop.dsb.domain.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RollType {

    ADVANTAGE("Advantage"),
    DISADVANTAGE("Disadvantage");

    private final String name;

}
