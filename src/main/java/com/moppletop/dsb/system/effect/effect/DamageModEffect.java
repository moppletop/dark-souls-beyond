package com.moppletop.dsb.system.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.system.effect.EffectKey;

import java.util.Map;

@EffectKey("DAMAGE_MOD")
public class DamageModEffect extends Effect {

    public DamageModEffect(Map<String, JsonNode> parameters) {
        super(parameters);
    }

    @Override
    protected String getDefaultDescription() {
        return "Reduce all damage taken by " + parameters.get("reduce").asInt();
    }
}
