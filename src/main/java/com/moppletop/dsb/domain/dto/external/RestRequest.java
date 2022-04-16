package com.moppletop.dsb.domain.dto.external;

import com.moppletop.dsb.domain.dto.game.RestType;
import lombok.Value;

@Value
public class RestRequest {

    Integer position;
    Integer positionDiceSpent;
    RestType type;

}
