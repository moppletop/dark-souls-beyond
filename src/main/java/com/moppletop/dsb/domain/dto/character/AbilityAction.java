package com.moppletop.dsb.domain.dto.character;

import com.moppletop.dsb.config.annotation.effect.EffectSource;
import com.moppletop.dsb.domain.dto.game.ActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@AllArgsConstructor
@Data
@EqualsAndHashCode(of = {"actionType", "name"})
public class AbilityAction implements Comparable<AbilityAction> {

    String name;
    ActionType actionType;
    final EffectSource source;
    String description;
    Integer uses;

    @Override
    public int compareTo(@NonNull AbilityAction o) {
        if (actionType != o.actionType) {
            return actionType.compareTo(o.actionType);
        }

        return name.compareTo(o.name);
    }
}
