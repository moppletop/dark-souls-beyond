package com.moppletop.dsb.domain.character;

import com.moppletop.dsb.domain.game.Dice;
import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.system.effect.effect.EffectSource;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
public class CharacterClass implements Comparable<CharacterClass>, EffectSource {

    public static final int[] ABILITY_IMPROVEMENT_LEVELS = {4, 8, 12, 16};

    int id;
    String name;
    String description;

    Dice positionDice;
    List<Effect> bloodiedEffects;

    List<String> startingItems;

    SpellCaster spellCaster;
    CharacterClassSpecialSlots specialSlots;
    Map<Integer, List<CharacterClassFeature>> levelFeatures;

    public boolean isSpellCaster() {
        return spellCaster != null;
    }

    @Override
    public int compareTo(@NonNull CharacterClass o) {
        return Integer.compare(id, o.id);
    }
}
