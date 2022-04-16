package com.moppletop.dsb.domain.dto.character;

import com.moppletop.dsb.domain.dto.game.Calculations;
import com.moppletop.dsb.domain.dto.game.RollType;
import lombok.Value;

@Value
public class Modification {

    int mod;
    RollType rollType;

    public String getModSigned() {
        return Calculations.getSignedNumber(mod);
    }

}
