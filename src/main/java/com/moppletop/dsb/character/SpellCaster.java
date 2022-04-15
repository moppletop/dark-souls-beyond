package com.moppletop.dsb.character;

import com.moppletop.dsb.game.Ability;
import com.moppletop.dsb.spell.SpellFamily;
import lombok.Value;

@Value
public class SpellCaster {

    Ability ability;
    SpellFamily spellFamily;
    String[] innateSpells;

}
