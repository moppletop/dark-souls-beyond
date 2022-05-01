package com.moppletop.dsb.system.dice;

import lombok.Value;

@Value
public class RollDiceResponse {

    Integer total;
    Integer[] rolls;

}
