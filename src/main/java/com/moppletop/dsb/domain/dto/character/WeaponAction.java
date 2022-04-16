package com.moppletop.dsb.domain.dto.character;

import com.moppletop.dsb.config.annotation.Item;
import com.moppletop.dsb.domain.dto.game.ActionType;
import com.moppletop.dsb.domain.dto.game.Calculations;
import com.moppletop.dsb.domain.dto.game.DamageType;
import com.moppletop.dsb.domain.dto.game.Dice;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class WeaponAction implements Comparable<WeaponAction> {

    final Item weapon;
    ActionType actionType;
    String rangeDescription;

    int hitBonus;
    int damageDiceCount;
    Dice damageDice;
    int flatDamage;
    DamageType damageType;

    String note;

    public String getDamage() {
        return damageDice.getExpression(damageDiceCount) + Calculations.getSignedNumber(flatDamage) + ' ' + damageType.getName();
    }

    public void addHitBonus(int bonus) {
        hitBonus += bonus;
    }

    public void addFlatDamage(int damage) {
        flatDamage += damage;
    }

    @Override
    public int compareTo(@NonNull WeaponAction o) {
        if (actionType != o.actionType) {
            return actionType.compareTo(o.actionType);
        }

        return weapon.compareTo(o.weapon);
    }
}
