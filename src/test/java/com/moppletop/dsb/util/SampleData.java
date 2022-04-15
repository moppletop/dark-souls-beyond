package com.moppletop.dsb.util;

import com.moppletop.dsb.character.PlayerCharacterService;
import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.user.User;
import com.moppletop.dsb.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Component
public class SampleData {

    private final UserService userService;
    private final PlayerCharacterService playerCharacterService;

    private final List<User> userSample = new ArrayList<>();
    private final List<PlayerCharacter> playerCharacterSample = new ArrayList<>();

    @Value("${dsb.test.sample_size}")
    @Autowired(required = false)
    private Integer sampleSize;

    @PostConstruct
    void generate() {
        if (sampleSize == null) {
            return;
        }

        registerSampleUsers(userService);
        registerSampleCharacters(playerCharacterService);
    }

    public PlayerCharacter getRandomPlayerCharacter() {
        Random random = ThreadLocalRandom.current();

        return playerCharacterSample.get(random.nextInt(playerCharacterSample.size()));
    }

    private void registerSampleCharacters(PlayerCharacterService playerCharacterService) {
        Random random = ThreadLocalRandom.current();

        for (int i = 1; i <= sampleSize; i++) {
            playerCharacterSample.add(playerCharacterService.createCharacter(userSample.get(random.nextInt(userSample.size())).getId()));
        }
    }

//    private static PlayerCharacter getRandomPlayerCharacter(int id, User user) {
//        Random random = ThreadLocalRandom.current();
//        return new PlayerCharacter(id, "Character-" + id, user, PlayerCharacterClass.values()[random.nextInt(PlayerCharacterClass.values().length)], 1);
//    }

    private void registerSampleUsers(UserService userService) {
        for (int i = 1; i <= sampleSize; i++) {
            userSample.add(userService.createUser("User-" + i, "password" + i));
        }
    }

}
