package com.moppletop.dsb.system.dice;

import com.moppletop.dsb.exception.PlayerCharacterInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;

@RestController
@RequestMapping("/api/dice")
@RequiredArgsConstructor
public class DiceController {

    private final DiceRollingService diceRollingService;

    @GetMapping("/roll")
    public ResponseEntity<RollDiceResponse> roll(@RequestParam String expression) {
        LinkedList<Integer> result = diceRollingService.parseExpression(expression);

        if (result == null || result.isEmpty()) {
            throw new PlayerCharacterInvalidException("Invalid expression"); // TODO use real exception
        }

        int total = result.pollFirst();
        Integer[] rolls = result.toArray(new Integer[0]);

        return ResponseEntity.ok(new RollDiceResponse(total, rolls));
    }

}
