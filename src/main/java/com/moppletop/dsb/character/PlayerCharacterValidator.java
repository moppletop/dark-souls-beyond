package com.moppletop.dsb.character;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PlayerCharacterValidator {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\w\\d\\s\\p{Punct}]{1,32}$");

    public static final String INVALID_NAME = "Player name must be between 1 and 32 characters and contain no invalid characters";
    public static final String INVALID_LEVEL = "Player level must be an integer between 1 and 20";

    public static String validateName(String name) {
        if (name == null) {
            throw new PlayerCharacterInvalidException(INVALID_NAME);
        }

        name = name.trim();

        if (!NAME_PATTERN.matcher(name).find()) {
            throw new PlayerCharacterInvalidException(INVALID_NAME);
        }

        return name;
    }

    public static void validateLevel(Integer level) {
        if (level == null || level < 1 || level > 20) {
            throw new PlayerCharacterInvalidException(INVALID_LEVEL);
        }
    }

}
