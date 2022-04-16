package com.moppletop.dsb.config.annotation.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.config.annotation.Effect;
import com.moppletop.dsb.config.annotation.EffectKey;
import com.moppletop.dsb.config.annotation.Item;
import com.moppletop.dsb.domain.dto.character.InventoryItem;
import com.moppletop.dsb.domain.dto.character.PlayerCharacter;
import com.moppletop.dsb.domain.dto.character.WeaponAction;
import com.moppletop.dsb.domain.dto.game.InventorySlot;

import java.util.Map;

@EffectKey("FIGHTING_STYLE_DUELING")
public class FightingStyleDuelingEffect extends Effect {

    public FightingStyleDuelingEffect(Map<String, JsonNode> parameters) {
        super(parameters);
    }

    @Override
    public void onWeaponAction(PlayerCharacter character, WeaponAction action) {
        Item weapon = action.getWeapon();

        if (weapon.getCategory().isBow()) {
            return;
        }

        for (InventoryItem inventoryItem : character.getInventory()) {
            if (inventoryItem.getSlot() == InventorySlot.RIGHT_HAND_WEAPON && inventoryItem.getItem().equals(weapon)) {
                action.addFlatDamage(2);
                return;
            }
        }
    }
}
