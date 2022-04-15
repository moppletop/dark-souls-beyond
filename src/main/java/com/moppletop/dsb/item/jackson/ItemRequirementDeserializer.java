package com.moppletop.dsb.item.jackson;

import com.moppletop.dsb.item.ItemRequirement;
import com.moppletop.dsb.item.ItemRequirementKey;
import com.moppletop.dsb.jacksonutil.KeyValueDeserializerBase;

public class ItemRequirementDeserializer extends KeyValueDeserializerBase<ItemRequirement, ItemRequirementKey> {

    public ItemRequirementDeserializer() {
        super(ItemRequirement.class, ItemRequirementKey.class, ItemRequirementKey::value);
    }
}
