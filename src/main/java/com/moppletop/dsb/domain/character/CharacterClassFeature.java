package com.moppletop.dsb.domain.character;

import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.system.effect.effect.EffectSource;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;

@Data
@EqualsAndHashCode(of = "name")
public class CharacterClassFeature implements EffectSource {

    final String name;

    // Static effects
    List<Effect> effects = Collections.emptyList();

    // Chosen effects
    int allowedOptions;
    List<CharacterClassFeature> options = Collections.emptyList();
    List<String> commonOptions = Collections.emptyList();

}
