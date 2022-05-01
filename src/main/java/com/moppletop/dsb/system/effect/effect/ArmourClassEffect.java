package com.moppletop.dsb.system.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.system.effect.EffectKey;
import com.moppletop.dsb.domain.character.PlayerCharacter;
import com.moppletop.dsb.domain.game.Ability;
import com.moppletop.dsb.domain.game.Calculations;

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
