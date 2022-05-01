package com.moppletop.dsb.controller.api.model;

import com.moppletop.dsb.domain.game.InventorySlot;
import lombok.Value;

@Value
public class AddItemRequest {

    String item;
    Integer amount;
    InventorySlot slot;

}
