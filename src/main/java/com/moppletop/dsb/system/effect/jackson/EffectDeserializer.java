package com.moppletop.dsb.system.effect.jackson;

import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.system.effect.EffectKey;
import com.moppletop.dsb.jacksonutil.KeyValueDeserializerBase;

public class EffectDeserializer extends KeyValueDeserializerBase<Effect, EffectKey> {

    public EffectDeserializer() {
        super(Effect.class, EffectKey.class, EffectKey::value);
    }
}
