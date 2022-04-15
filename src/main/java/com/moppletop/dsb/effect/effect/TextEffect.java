package com.moppletop.dsb.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.effect.Effect;
import com.moppletop.dsb.effect.EffectKey;

import java.util.Map;

@EffectKey("TEXT")
class TextEffect extends Effect {

    public TextEffect(Map<String, JsonNode> parameters) {
        super(parameters);
    }

}
