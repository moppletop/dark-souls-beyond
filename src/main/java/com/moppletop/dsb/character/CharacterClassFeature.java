package com.moppletop.dsb.character;

import com.moppletop.dsb.effect.Effect;
import com.moppletop.dsb.effect.EffectSource;
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
