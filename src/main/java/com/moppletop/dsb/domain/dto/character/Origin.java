package com.moppletop.dsb.domain.dto.character;

import com.moppletop.dsb.config.annotation.Effect;
import com.moppletop.dsb.config.annotation.effect.EffectSource;
import com.moppletop.dsb.domain.dto.game.Ability;
import com.moppletop.dsb.domain.dto.game.Dice;
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
