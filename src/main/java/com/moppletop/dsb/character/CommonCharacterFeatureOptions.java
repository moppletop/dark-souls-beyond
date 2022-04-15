package com.moppletop.dsb.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommonCharacterFeatureOptions {

    private final ObjectMapper objectMapper;

    private final Map<String, CharacterClassFeature> featuresByName = new HashMap<>();

    @PostConstruct
    void generateOptions() throws IOException {
        File dir = new File("source", "options");

        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".json"));

        if (files == null) {
            return;
        }

        for (File file : files) {
            CharacterClassFeature[] features = objectMapper.readValue(file, CharacterClassFeature[].class);

            for (CharacterClassFeature feature : features) {
                featuresByName.put(feature.getName(), feature);
            }
        }

        log.info("Loaded {} common feature options", featuresByName.size());
    }

    public Optional<CharacterClassFeature> getByName(String name) {
        return Optional.ofNullable(featuresByName.get(name));
    }


}
