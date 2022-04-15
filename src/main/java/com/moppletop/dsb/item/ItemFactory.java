package com.moppletop.dsb.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemFactory {

    private final ObjectMapper objectMapper;

    private final Map<String, Item> itemsByName = new HashMap<>();
    private final Map<ItemCategory, List<Item>> itemsByCategory = new HashMap<>();

    private Item estusFlask;

    @PostConstruct
    void generateItems() throws IOException {
        File dir = new File("source", "items");

        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        File[] files = dir.listFiles((dir1, name) -> name.endsWith(".json"));

        if (files == null) {
            return;
        }

        for (ItemCategory category : ItemCategory.values()) {
            itemsByCategory.put(category, new LinkedList<>());
        }

        for (File file : files) {
            Item[] items = objectMapper.readValue(file, Item[].class);

            for (Item item : items) {
                if (item.getName().equals("Estus Flask")) {
                    estusFlask = item;
                }

                itemsByName.put(item.getName(), item);
                itemsByCategory.computeIfAbsent(item.getCategory(), k -> new LinkedList<>()).add(item);
            }
        }

        log.info("Loaded {} items", itemsByName.size());
    }

    public Optional<Item> getByName(String name) {
        return Optional.ofNullable(itemsByName.get(name));
    }

    public List<Item> search(String name, Set<ItemCategory> categories) {
        Stream<Item> stream = itemsByName.values().stream()
                .filter(item -> item.getHidden() == null || !item.getHidden());

        if (categories != null && !categories.isEmpty()) {
            stream = stream.filter(item -> categories.contains(item.getCategory()));
        }

        if (name != null) {
            String nameLower = name.toLowerCase();
            stream = stream.filter(item -> item.getName().toLowerCase().contains(nameLower));
        }

        stream = stream.sorted()
                .limit(25);

        return stream.collect(Collectors.toList());
    }

    public Item getEstusFlask() {
        return estusFlask;
    }
}
