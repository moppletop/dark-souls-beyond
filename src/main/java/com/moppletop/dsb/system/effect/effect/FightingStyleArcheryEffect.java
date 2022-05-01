package com.moppletop.dsb.system.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.system.effect.EffectKey;
import com.moppletop.dsb.system.effect.Item;
import com.moppletop.dsb.domain.character.PlayerCharacter;
import com.moppletop.dsb.domain.character.WeaponAction;

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
