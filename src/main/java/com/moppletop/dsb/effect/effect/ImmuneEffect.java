package com.moppletop.dsb.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.game.ResistanceType;
import com.moppletop.dsb.effect.Effect;
import com.moppletop.dsb.effect.EffectKey;
import com.moppletop.dsb.effect.EffectSource;
import com.moppletop.dsb.game.DamageType;

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
