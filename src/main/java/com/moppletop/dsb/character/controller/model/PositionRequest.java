package com.moppletop.dsb.character.controller.model;

import lombok.Value;

@Value
public class PositionRequest {

    Integer position;
    Action action;

    public enum Action {

        MOD,
        SET,
        START_COMBAT,
        STOP_COMBAT

    }

}
