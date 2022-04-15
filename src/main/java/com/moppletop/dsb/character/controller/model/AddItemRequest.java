package com.moppletop.dsb.character.controller.model;

import com.moppletop.dsb.game.InventorySlot;
import lombok.Value;

@Value
public class AddItemRequest {

    String item;
    Integer amount;
    InventorySlot slot;

}
