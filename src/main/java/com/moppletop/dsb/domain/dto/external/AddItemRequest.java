package com.moppletop.dsb.domain.dto.external;

import com.moppletop.dsb.domain.dto.game.InventorySlot;
import lombok.Value;

@Value
public class AddItemRequest {

    String item;
    Integer amount;
    InventorySlot slot;

}
