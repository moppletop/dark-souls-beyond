package com.moppletop.dsb.system.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.system.effect.EffectKey;
import com.moppletop.dsb.domain.character.PlayerCharacter;
import com.moppletop.dsb.domain.game.DamageType;
import com.moppletop.dsb.domain.game.ResistanceType;

import java.util.Map;

@EffectKey("IMMUNE")
class ImmuneEffect extends Effect {

    private final DamageType damageType;

    public ImmuneEffect(Map<String, JsonNode> parameters) {
        super(parameters);

        this.damageType = getEnum("damageType", DamageType::valueOf);
    }

    @Override
    public void onApply(PlayerCharacter character, EffectSource source) {
        character.addResistance(source.getName(), damageType.getName(), ResistanceType.IMMUNITY);
    }

    @Override
    protected String getDefaultDescription() {
        return "Gain immunity to " + damageType.getName() + " damage.";
    }
}
