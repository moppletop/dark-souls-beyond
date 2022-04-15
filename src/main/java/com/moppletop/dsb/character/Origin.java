package com.moppletop.dsb.character;

import com.moppletop.dsb.effect.EffectSource;
import com.moppletop.dsb.game.Ability;
import com.moppletop.dsb.game.Dice;
import com.moppletop.dsb.effect.Effect;
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
