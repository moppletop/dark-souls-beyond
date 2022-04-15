package com.moppletop.dsb.character;

import com.moppletop.dsb.effect.EffectSource;
import com.moppletop.dsb.game.ActionType;
import lombok.*;

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
