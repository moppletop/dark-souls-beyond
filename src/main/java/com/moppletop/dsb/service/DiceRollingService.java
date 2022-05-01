package com.moppletop.dsb.service;

import com.moppletop.dsb.domain.dto.game.Dice;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DiceRollingService {

    private static final Pattern ROLL_EXPRESSION = Pattern.compile("^(\\d+)(D\\d+)(([+-])(\\d+))?$");

    public LinkedList<Integer> parseExpression(String expression) {
        expression = expression.toUpperCase();

        LinkedList<Integer> rolls = new LinkedList<>();
        Matcher matcher = ROLL_EXPRESSION.matcher(expression);

        if (matcher.find()) {
            int numberOfDice = Integer.parseInt(matcher.group(1));
            Dice dice = Dice.valueOf(matcher.group(2));

            int totalRoll = 0;
            Random random = ThreadLocalRandom.current();

            for (int i = 0; i < numberOfDice; i++) {
                int thisRoll = random.nextInt(dice.getMax()) + 1;
                rolls.add(thisRoll);
                totalRoll += thisRoll;
            }

            if (matcher.group(5) != null) {
                int flatMod = Integer.parseInt(matcher.group(5));

                if (matcher.group(4).equals("-")) {
                    totalRoll -= flatMod;
                } else {
                    totalRoll += flatMod;
                }
            }

            rolls.addFirst(totalRoll);

            return rolls;
        }

        return null;
    }
}
