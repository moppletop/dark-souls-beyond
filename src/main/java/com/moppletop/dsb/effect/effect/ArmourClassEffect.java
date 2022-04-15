package com.moppletop.dsb.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.game.Ability;
import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.effect.Effect;
import com.moppletop.dsb.effect.EffectKey;
import com.moppletop.dsb.effect.EffectSource;
import com.moppletop.dsb.game.Calculations;

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
