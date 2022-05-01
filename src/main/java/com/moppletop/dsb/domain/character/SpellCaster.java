package com.moppletop.dsb.domain.character;

import com.moppletop.dsb.domain.game.Ability;
import com.moppletop.dsb.domain.spell.SpellFamily;
import lombok.Value;

@Value
public class SpellCaster {

    Ability ability;
    SpellFamily spellFamily;
    String[] innateSpells;

}
