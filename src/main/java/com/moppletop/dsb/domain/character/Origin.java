package com.moppletop.dsb.domain.character;

import com.moppletop.dsb.domain.game.Ability;
import com.moppletop.dsb.domain.game.Dice;
import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.system.effect.effect.EffectSource;
import lombok.NonNull;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
public class Origin implements EffectSource, Comparable<Origin> {

    int id;
    String name;
    String description;
    String goodAt;

    Dice positionDice;
    List<Effect> bloodiedEffects;

    Map<Ability, Integer> abilityScores;

    @Override
    public int compareTo(@NonNull Origin o) {
        return name.compareTo(o.name);
    }

}
