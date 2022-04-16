package com.moppletop.dsb.config.annotation.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.config.annotation.Effect;
import com.moppletop.dsb.config.annotation.EffectKey;
import com.moppletop.dsb.domain.dto.character.PlayerCharacter;
import com.moppletop.dsb.domain.dto.game.DamageType;
import com.moppletop.dsb.domain.dto.game.ResistanceType;

import java.util.Map;

@EffectKey("RESISTANCE")
class ResistanceEffect extends Effect {

    private final DamageType damageType;

    public ResistanceEffect(Map<String, JsonNode> parameters) {
        super(parameters);

        this.damageType = getEnum("damageType", DamageType::valueOf);
    }

    @Override
    public void onApply(PlayerCharacter character, EffectSource source) {
        character.addResistance(source.getName(), damageType.getName(), ResistanceType.RESISTANCE);
    }

    @Override
    protected String getDefaultDescription() {
        return "Gain resistance to " + damageType.getName() + " damage.";
    }
}
