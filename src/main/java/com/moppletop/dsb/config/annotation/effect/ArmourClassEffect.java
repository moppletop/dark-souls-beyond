package com.moppletop.dsb.config.annotation.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.config.annotation.Effect;
import com.moppletop.dsb.config.annotation.EffectKey;
import com.moppletop.dsb.domain.dto.character.PlayerCharacter;
import com.moppletop.dsb.domain.dto.game.Ability;
import com.moppletop.dsb.domain.dto.game.Calculations;

import java.util.Map;

@EffectKey("ARMOUR_CLASS")
class ArmourClassEffect extends Effect {

    private final int mod;
    private final Ability abilityScaling;

    public ArmourClassEffect(Map<String, JsonNode> parameters) {
        super(parameters);

        mod = parameters.get("mod").asInt();
        abilityScaling = getAbility();
    }

    @Override
    public void onPostApply(PlayerCharacter character, EffectSource source) {
        int mod = this.mod;

        if (abilityScaling != null) {
            mod += Calculations.getAbilityScoreModifier(character.getAbilityScore(abilityScaling));
        }

        character.modifyArmourClass(source.getName(), mod);
    }

    @Override
    protected String getDefaultDescription() {
        return "Increase your AC by " + mod + (abilityScaling == null ? "" : " + your " + abilityScaling.getName() + " modifier");
    }
}
