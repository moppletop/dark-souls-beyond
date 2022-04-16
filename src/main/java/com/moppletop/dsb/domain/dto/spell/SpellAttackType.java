package com.moppletop.dsb.domain.dto.spell;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SpellAttackType {

    ATTACK_ROLL("Attack Roll"),
    SAVING_THROW("Saving Throw")
    ;

    private final String name;

}