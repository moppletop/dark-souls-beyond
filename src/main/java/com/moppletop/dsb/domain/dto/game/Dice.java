package com.moppletop.dsb.domain.dto.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Dice {

    D4(4),
    D6(6),
    D8(8),
    D10(10),
    D12(12),
    D20(20);

    private final int max;

    public String getExpression(int count) {
        return count + "d" + max;
    }
}
