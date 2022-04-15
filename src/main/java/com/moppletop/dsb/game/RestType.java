package com.moppletop.dsb.game;

public enum RestType {

    SHORT,
    LONG;

    public boolean includes(RestType other) {
        return other.ordinal() <= ordinal();
    }

}
