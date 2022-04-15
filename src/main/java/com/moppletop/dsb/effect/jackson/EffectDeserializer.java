package com.moppletop.dsb.effect.jackson;

import com.moppletop.dsb.effect.Effect;
import com.moppletop.dsb.effect.EffectKey;
import com.moppletop.dsb.jacksonutil.KeyValueDeserializerBase;

public class EffectDeserializer extends KeyValueDeserializerBase<Effect, EffectKey> {

    public EffectDeserializer() {
        super(Effect.class, EffectKey.class, EffectKey::value);
    }
}
