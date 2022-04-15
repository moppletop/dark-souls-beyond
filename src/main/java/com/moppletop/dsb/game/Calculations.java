package com.moppletop.dsb.game;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Calculations {

    public static int getProficiencyBonus(int level) {
        if (level < 5) {
            return 2;
        } else if (level < 9) {
            return 3;
        } else if (level < 13) {
            return 4;
        } else if (level < 17) {
            return 5;
        }

        return 6;
    }

    public static int getAbilityScoreModifier(int abilityScore) {
        return Math.floorDiv(abilityScore - 10, 2);
    }

    public static int getSoulsForLevel(int level) {
        switch (level) {
            default:
                return 0;
            case 2:
                return 300;
            case 3:
                return 900;
            case 4:
                return 2700;
            case 5:
                return 6500;
            case 6:
                return 14000;
            case 7:
                return 23000;
            case 8:
                return 34000;
            case 9:
                return 48000;
            case 10:
                return 64000;
            case 11:
                return 85000;
            case 12:
                return 100000;
            case 13:
                return 120000;
            case 14:
                return 140000;
            case 15:
                return 165000;
            case 16:
                return 195000;
            case 17:
                return 225000;
            case 18:
                return 265000;
            case 19:
                return 305000;
            case 20:
                return 355000;
        }
    }

    public static int getMaxPosition(Dice positionDice, int level, int constitution) {
        int constitutionMod = getAbilityScoreModifier(constitution);
        int position = positionDice.getMax() + constitutionMod + 1;

        for (int i = 2; i <= level; i++) {
            position += Math.max(1, i + constitutionMod); // A level can never decrease position
        }

        return position;
    }

    public static int getEstusUses(int level) {
        if (level < 5) {
            return 3;
        } else if (level < 10) {
            return 4;
        } else if (level < 17) {
            return 5;
        }

        return 6;
    }

    public static String getSignedNumber(int number) {
        return number >= 0 ? "+" + number : String.valueOf(number);
    }

}
