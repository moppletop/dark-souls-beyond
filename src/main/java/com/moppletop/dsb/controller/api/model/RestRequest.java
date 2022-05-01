package com.moppletop.dsb.controller.api.model;

import com.moppletop.dsb.domain.game.RestType;
import lombok.Value;

@Value
public class RestRequest {

    Integer position;
    Integer positionDiceSpent;
    RestType type;

}
