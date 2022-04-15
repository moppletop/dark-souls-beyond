package com.moppletop.dsb.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class OriginFactory {

    private final ObjectMapper objectMapper;
    private final Map<Integer, Origin> originsById = new HashMap<>();

    @PostConstruct
    void generateItems() throws IOException {
        File dir = new File("source", "origins");

        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".json"));

        if (files == null) {
            return;
        }

        for (File file : files) {
            Origin origin = objectMapper.readValue(file, Origin.class);
            originsById.put(origin.getId(), origin);
        }

        log.info("Loaded {} origins", originsById.size());
    }

    public Optional<Origin> getById(int originId) {
        return Optional.ofNullable(originsById.get(originId));
    }

    public List<Origin> getAll() {
        return originsById.values().stream()
                .sorted()
                .collect(Collectors.toList());
    }

}
