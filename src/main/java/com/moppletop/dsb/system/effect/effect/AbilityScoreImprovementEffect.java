package com.moppletop.dsb.system.effect.effect;

import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.domain.character.PlayerCharacter;
import com.moppletop.dsb.domain.game.Ability;

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
