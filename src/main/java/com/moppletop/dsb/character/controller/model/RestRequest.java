package com.moppletop.dsb.character.controller.model;

import com.moppletop.dsb.game.RestType;
import lombok.Value;

@Value
public class RestRequest {

    Integer position;
    Integer positionDiceSpent;
    RestType type;

}
