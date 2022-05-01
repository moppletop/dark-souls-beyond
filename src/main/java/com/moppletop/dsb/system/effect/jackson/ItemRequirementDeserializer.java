package com.moppletop.dsb.system.effect.jackson;

import com.moppletop.dsb.system.effect.ItemRequirementKey;
import com.moppletop.dsb.system.effect.item.ItemRequirement;
import com.moppletop.dsb.jacksonutil.KeyValueDeserializerBase;

public class ItemRequirementDeserializer extends KeyValueDeserializerBase<ItemRequirement, ItemRequirementKey> {

    public ItemRequirementDeserializer() {
        super(ItemRequirement.class, ItemRequirementKey.class, ItemRequirementKey::value);
    }
}
