package com.moppletop.dsb.domain.game;

import lombok.Value;

@Value
public class Resistance {

    String source;
    String to;
    ResistanceType type;

    @Override
    public String toString() {
        return source + ": " + type.getName() + " to " + to;
    }

}
