package com.moppletop.dsb.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.character.WeaponAction;
import com.moppletop.dsb.effect.Effect;
import com.moppletop.dsb.effect.EffectKey;
import com.moppletop.dsb.item.Item;

import java.util.Map;

@EffectKey("FIGHTING_STYLE_ARCHERY")
public class FightingStyleArcheryEffect extends Effect {

    public FightingStyleArcheryEffect(Map<String, JsonNode> parameters) {
        super(parameters);
    }

    @Override
    public void onWeaponAction(PlayerCharacter character, WeaponAction action) {
        Item weapon = action.getWeapon();

        if (weapon.getCategory().isBow()) {
            action.addHitBonus(2);
        }
    }
}
