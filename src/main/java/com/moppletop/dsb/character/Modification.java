package com.moppletop.dsb.character;

import com.moppletop.dsb.game.Calculations;
import com.moppletop.dsb.game.RollType;
import lombok.Value;

@Value
public class Modification {

    int mod;
    RollType rollType;

    public String getModSigned() {
        return Calculations.getSignedNumber(mod);
    }

}
