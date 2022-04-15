package com.moppletop.dsb.effect.effect;

import com.moppletop.dsb.game.Ability;
import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.effect.Effect;
import com.moppletop.dsb.effect.EffectSource;

public class AbilityScoreImprovementEffect extends Effect {

    private final Ability ability;

    public AbilityScoreImprovementEffect(Ability ability) {
        super(null);

        this.ability = ability;
    }

    @Override
    public void onApply(PlayerCharacter character, EffectSource source) {
        character.modifyAbilityScore(source.getName(), ability, 1);
    }

    @Override
    protected String getDefaultDescription() {
        return "Increase your " + ability.getName() + " by 1.";
    }
}
