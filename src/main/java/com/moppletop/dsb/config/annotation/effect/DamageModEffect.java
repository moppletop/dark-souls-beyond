package com.moppletop.dsb.config.annotation.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.config.annotation.Effect;
import com.moppletop.dsb.config.annotation.EffectKey;

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
