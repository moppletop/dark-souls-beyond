package com.moppletop.dsb.config.annotation.jackson;

import com.moppletop.dsb.config.annotation.Effect;
import com.moppletop.dsb.config.annotation.EffectKey;
import com.moppletop.dsb.jacksonutil.KeyValueDeserializerBase;

public class EffectDeserializer extends KeyValueDeserializerBase<Effect, EffectKey> {

    public EffectDeserializer() {
        super(Effect.class, EffectKey.class, EffectKey::value);
    }
}
