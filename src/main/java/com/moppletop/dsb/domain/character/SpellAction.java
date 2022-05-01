package com.moppletop.dsb.domain.character;

import com.moppletop.dsb.domain.game.Ability;
import com.moppletop.dsb.domain.game.ActionType;
import com.moppletop.dsb.domain.spell.SpellAttackType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = {"actionType", "name"})
public class SpellAction implements Comparable<SpellAction> {

    final String name;
    ActionType actionType;
    String rangeDescription;

    SpellAttackType attackType;
    Integer hitBonus;
    Integer difficultyClass;
    Ability savingThrowType;

    String damage;

    String note;

    public String getHitBonusOrDC() {
        if (attackType == null) {
            return "-";
        }

        switch (attackType) {
            case ATTACK_ROLL:
                return "+" + hitBonus;
            case SAVING_THROW:
                return "DC" + difficultyClass + " " + savingThrowType.getShorthand();
            default:
                return "-";
        }
    }

    @Override
    public int compareTo(@NonNull SpellAction o) {
        if (actionType != o.actionType) {
            return actionType.compareTo(o.actionType);
        }

        return name.compareTo(o.name);
    }
}
