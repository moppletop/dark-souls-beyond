package com.moppletop.dsb.domain.character;

import lombok.Value;

import java.util.List;

@Value
public class CharacterClassSpecialSlots {

    String name;
    List<Integer> levels;

}
