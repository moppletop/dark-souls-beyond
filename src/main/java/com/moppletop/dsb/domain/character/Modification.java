package com.moppletop.dsb.domain.character;

import com.moppletop.dsb.domain.game.Calculations;
import com.moppletop.dsb.domain.game.RollType;
import lombok.Value;

@Value
public class Modification {

    int mod;
    RollType rollType;

    public String getModSigned() {
        return Calculations.getSignedNumber(mod);
    }

}
