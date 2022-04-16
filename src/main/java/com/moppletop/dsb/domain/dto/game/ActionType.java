package com.moppletop.dsb.domain.dto.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ActionType {

    ACTION("Action"),
    BONUS("Bonus Action"),
    REACTION("Reaction"),
    OTHER("Other");

    private final String name;

}
