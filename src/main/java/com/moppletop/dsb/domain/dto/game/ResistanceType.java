package com.moppletop.dsb.domain.dto.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResistanceType {

    RESISTANCE("Resistant"),
    VULNERABILITY("Vulnerable"),
    IMMUNITY("Immune");

    private final String name;

}
