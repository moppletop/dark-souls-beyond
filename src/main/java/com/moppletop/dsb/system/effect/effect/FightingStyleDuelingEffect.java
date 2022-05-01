package com.moppletop.dsb.system.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.system.effect.EffectKey;
import com.moppletop.dsb.system.effect.Item;
import com.moppletop.dsb.domain.character.InventoryItem;
import com.moppletop.dsb.domain.character.PlayerCharacter;
import com.moppletop.dsb.domain.character.WeaponAction;
import com.moppletop.dsb.domain.game.InventorySlot;

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
