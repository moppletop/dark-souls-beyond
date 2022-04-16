package com.moppletop.dsb.domain.dto.character;

import com.moppletop.dsb.domain.dto.spell.Spell;
import lombok.NonNull;
import lombok.Value;

@Value
public class LearntSpell implements Comparable<LearntSpell> {

    Spell spell;
    int spentCasts;
    boolean attuned;

    @Override
    public int compareTo(@NonNull LearntSpell o) {
        if (attuned != o.attuned) {
            return Boolean.compare(attuned, o.attuned);
        }

        return spell.compareTo(o.spell);
    }
}
