package com.moppletop.dsb.domain.dto.character;

import com.moppletop.dsb.domain.dto.game.Ability;
import com.moppletop.dsb.domain.dto.spell.SpellFamily;
import lombok.Value;

@Value
public class SpellCaster {

    Ability ability;
    SpellFamily spellFamily;
    String[] innateSpells;

}
