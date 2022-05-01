package com.moppletop.dsb.controller.api.dice;

import lombok.Value;

@Value
public class RollDiceResponse {

    Integer total;
    Integer[] rolls;

}
