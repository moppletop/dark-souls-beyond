package com.moppletop.dsb.config.annotation.jackson;

import com.moppletop.dsb.config.annotation.ItemRequirementKey;
import com.moppletop.dsb.config.annotation.item.ItemRequirement;
import com.moppletop.dsb.jacksonutil.KeyValueDeserializerBase;

public class ItemRequirementDeserializer extends KeyValueDeserializerBase<ItemRequirement, ItemRequirementKey> {

    public ItemRequirementDeserializer() {
        super(ItemRequirement.class, ItemRequirementKey.class, ItemRequirementKey::value);
    }
}
