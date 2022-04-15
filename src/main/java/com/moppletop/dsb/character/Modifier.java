package com.moppletop.dsb.character;

import com.moppletop.dsb.game.Calculations;
import com.moppletop.dsb.game.RollType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public final class Modifier {

    private final String source;
    private final int mod;
    private final RollType rollType;

    public Modifier(String source, int mod) {
        this(source, mod, null);
    }

    public Modifier(String source, RollType rollType) {
        this(source, 0, rollType);
    }

    @Override
    public String toString() {
        return source + ": " + (rollType == null ? Calculations.getSignedNumber(mod) : rollType);
    }
}
