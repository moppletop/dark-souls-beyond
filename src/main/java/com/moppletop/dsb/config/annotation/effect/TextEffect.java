package com.moppletop.dsb.config.annotation.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.config.annotation.Effect;
import com.moppletop.dsb.config.annotation.EffectKey;

import java.util.Map;

@EffectKey("TEXT")
class TextEffect extends Effect {

    public TextEffect(Map<String, JsonNode> parameters) {
        super(parameters);
    }

}
