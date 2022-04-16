package com.moppletop.dsb.domain.dto.spell;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SpellFamily {

    PYROMANCY("Pyromancy"),
    SORCERY("Sorcery"),
    MIRACLE("Miracle");

    private final String name;

}
