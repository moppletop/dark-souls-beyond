package com.moppletop.dsb.config.annotation.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.config.annotation.Effect;
import com.moppletop.dsb.config.annotation.EffectKey;
import com.moppletop.dsb.config.annotation.Item;
import com.moppletop.dsb.domain.dto.character.PlayerCharacter;
import com.moppletop.dsb.domain.dto.character.WeaponAction;

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
