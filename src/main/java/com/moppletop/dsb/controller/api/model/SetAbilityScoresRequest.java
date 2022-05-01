package com.moppletop.dsb.controller.api.model;

import com.moppletop.dsb.domain.game.Ability;
import lombok.Value;

import java.util.Map;

@Value
public class SetAbilityScoresRequest {

    Map<Ability, Integer> scores;

}
