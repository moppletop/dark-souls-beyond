package com.moppletop.dsb.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.character.InventoryItem;
import com.moppletop.dsb.game.InventorySlot;
import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.character.WeaponAction;
import com.moppletop.dsb.effect.Effect;
import com.moppletop.dsb.effect.EffectKey;
import com.moppletop.dsb.item.Item;

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
