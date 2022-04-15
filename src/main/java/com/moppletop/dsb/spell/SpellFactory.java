package com.moppletop.dsb.spell;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class SpellFactory {

    private final ObjectMapper objectMapper;

    private final Map<String, Spell> spellByName = new HashMap<>();

    @PostConstruct
    void generateSpells() throws IOException {
        File dir = new File("source", "spells");

        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".json"));

        if (files == null) {
            return;
        }

        for (File file : files) {
            Spell[] spells = objectMapper.readValue(file, Spell[].class);

            for (Spell spell : spells) {
                spellByName.put(spell.getName(), spell);
            }
        }

        log.info("Loaded {} spells", spellByName.size());
    }

    public Optional<Spell> getByName(String name) {
        return Optional.ofNullable(spellByName.get(name));
    }

    public List<Spell> search(String name, SpellFamily family) {
        Stream<Spell> stream = spellByName.values().stream();

        if (family != null) {
            stream = stream.filter(spell -> spell.getFamily() == family);
        }

        if (name != null) {
            stream = stream.filter(spell -> spell.getName().contains(name));
        }

        stream = stream.sorted()
                .limit(25);

        return stream.collect(Collectors.toList());
    }
}
