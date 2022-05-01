package com.moppletop.dsb.system.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.system.effect.EffectKey;

import java.util.Map;

@EffectKey("TEXT")
class TextEffect extends Effect {

    public TextEffect(Map<String, JsonNode> parameters) {
        super(parameters);
    }

}
