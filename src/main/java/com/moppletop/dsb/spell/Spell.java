package com.moppletop.dsb.spell;

import com.moppletop.dsb.game.Ability;
import com.moppletop.dsb.game.ActionType;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Objects;

@Value
@EqualsAndHashCode(of = "name")
public class Spell implements Comparable<Spell> {

    String name;
    String description;
    SpellFamily family;
    Integer level;
    SpellAttackType attackType;
    Ability savingThrow;
    ActionType castAction;
    Integer castTurns = 1;
    Integer casts;
    String duration = "Instantaneous";
    Integer range; // 0 for Touch, -1 for Self
    Integer slots;
    Integer cost;

    public String getCastTime() {
        return castTurns + " " + castAction.getName() + (castTurns > 1 ? 's' : "");
    }

    public String getRangeDescription() {
        if (range == null) {
            return "-";
        }

        switch (range) {
            case -1:
                return "Self";
            case 0:
                return "Touch";
            default:
                return range + " ft.";
        }
    }

    @Override
    public int compareTo(Spell o) {
        if (!Objects.equals(level, o.level)) {
            return Integer.compare(level, o.level);
        }

        return name.compareTo(o.name);
    }
}
