package com.moppletop.dsb.system.player;

import com.moppletop.dsb.system.effect.Item;
import com.moppletop.dsb.domain.character.CharacterClass;
import com.moppletop.dsb.domain.character.CharacterClassFactory;
import com.moppletop.dsb.domain.character.CharacterClassFeature;
import com.moppletop.dsb.domain.character.NewPosition;
import com.moppletop.dsb.domain.character.Origin;
import com.moppletop.dsb.domain.character.PlayerCharacter;
import com.moppletop.dsb.domain.character.SpellCaster;
import com.moppletop.dsb.domain.game.Calculations;
import com.moppletop.dsb.domain.game.InventorySlot;
import com.moppletop.dsb.domain.game.RestType;
import com.moppletop.dsb.domain.spell.Spell;
import com.moppletop.dsb.domain.validation.PlayerCharacterValidator;
import com.moppletop.dsb.db.entity.ClassFeatureEntity;
import com.moppletop.dsb.db.entity.ClassFeatureOptionEntity;
import com.moppletop.dsb.db.entity.InventoryEntity;
import com.moppletop.dsb.db.entity.PlayerCharacterEntity;
import com.moppletop.dsb.db.entity.SpellEntity;
import com.moppletop.dsb.db.entity.UserEntity;
import com.moppletop.dsb.exception.ItemInvalidException;
import com.moppletop.dsb.exception.PlayerCharacterInvalidException;
import com.moppletop.dsb.exception.SpellInvalidException;
import com.moppletop.dsb.exception.UserInvalidException;
import com.moppletop.dsb.factory.ItemFactory;
import com.moppletop.dsb.factory.OriginFactory;
import com.moppletop.dsb.factory.SpellFactory;
import com.moppletop.dsb.db.repository.InventoryRepository;
import com.moppletop.dsb.db.repository.PlayerCharacterRepository;
import com.moppletop.dsb.db.repository.SpellRepository;
import com.moppletop.dsb.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerCharacterService {

    public static final String COMBAT_FEATURE = "Combat";

    private final PlayerCharacterRepository characterRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final SpellRepository spellRepository;

    private final CharacterClassFactory characterClassFactory;
    private final OriginFactory originFactory;
    private final ItemFactory itemFactory;
    private final SpellFactory spellFactory;

    private final ConversionService conversionService;

    public PlayerCharacter createCharacter(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(UserInvalidException::new);

        PlayerCharacterEntity characterEntity = new PlayerCharacterEntity();

        characterEntity.setName(userEntity.getName() + " - " + LocalDate.now());
        characterEntity.setUser(userEntity);

        characterEntity = characterRepository.save(characterEntity);

        return new PlayerCharacter(characterEntity.getId(), characterEntity.getUser().getId());
    }

    public List<PlayerCharacter> getCharacters(Integer userId) {
        return characterRepository.findByUserId(userId).stream()
                .map(this::fromEntity)
                .collect(Collectors.toList());
    }

    public PlayerCharacter getCharacter(Integer id) {
        PlayerCharacterEntity characterEntity = characterRepository.findById(id)
                .orElseThrow(PlayerCharacterInvalidException::new);

        return fromEntity(characterEntity);
    }

    public void deleteCharacter(Integer id) {
        characterRepository.deleteById(id);
    }

    public void setName(Integer characterId, String name) {
        PlayerCharacterEntity characterEntity = characterRepository.findById(characterId)
                .orElseThrow(PlayerCharacterInvalidException::new);

        PlayerCharacterValidator.validateName(name);

        characterEntity.setName(name);
        characterRepository.save(characterEntity);
    }

    public void setClass(Integer characterId, Integer classId) {
        PlayerCharacterEntity characterEntity = characterRepository.findById(characterId)
                .orElseThrow(PlayerCharacterInvalidException::new);

        if (Objects.equals(classId, characterEntity.getClassId())) {
            return;
        }

        CharacterClass characterClass = characterClassFactory.getById(classId)
                .orElseThrow(() -> new PlayerCharacterInvalidException(classId + " is not a valid class id"));

        characterEntity.setClassId(characterClass.getId());

        // Remove any class features
        characterEntity.getClassFeatures().clear();

        // Remove any spells
        characterEntity.getSpells().clear();

        // Remove starting items from previous class
        characterEntity.getInventory().removeIf(InventoryEntity::getStartingItem);

        // Add new class features
        int level = characterEntity.getLevel() == null ? 1 : characterEntity.getLevel();
        characterClass.getLevelFeatures().entrySet().stream()
                .filter(entry -> entry.getKey() <= level)
                .flatMap(entry -> entry.getValue().stream())
                .forEach(feature -> {
                    ClassFeatureEntity featureEntity = new ClassFeatureEntity();

                    featureEntity.setCharacter(characterEntity);
                    featureEntity.setFeature(feature.getName());

                    characterEntity.getClassFeatures().add(featureEntity);
                });

        // Add new starting items
        for (String itemName : characterClass.getStartingItems()) {
            InventoryEntity inventoryEntity = new InventoryEntity();

            inventoryEntity.setCharacter(characterEntity);
            inventoryEntity.setItem(itemName);
            inventoryEntity.setStartingItem(true);

            characterEntity.getInventory().add(inventoryEntity);
        }

        characterRepository.save(characterEntity);

        // Add innate spells
        if (characterClass.isSpellCaster()) {
            SpellCaster spellCaster = characterClass.getSpellCaster();

            if (spellCaster.getInnateSpells() != null) {
                for (String spellName : spellCaster.getInnateSpells()) {
                    spellFactory.getByName(spellName).ifPresent(spell -> learnSpell(characterId, spellName, true));
                }
            }
        }
    }

    public void setOrigin(Integer characterId, Integer originId) {
        PlayerCharacterEntity characterEntity = characterRepository.findById(characterId)
                .orElseThrow(PlayerCharacterInvalidException::new);

        if (Objects.equals(characterEntity.getOriginId(), originId)) {
            return;
        }

        Origin origin = originFactory.getById(originId)
                .orElseThrow(() -> new PlayerCharacterInvalidException(originId + " is not a valid origin id"));

        characterEntity.setOriginId(origin.getId());
        characterRepository.save(characterEntity);
    }

    public boolean levelUp(Integer characterId) {
        PlayerCharacterEntity characterEntity = characterRepository.findById(characterId)
                .orElseThrow(PlayerCharacterInvalidException::new);

        int newLevel = characterEntity.getLevel() + 1;

        PlayerCharacterValidator.validateLevel(newLevel);

        // Do they have the souls?
        int cost = Calculations.getSoulsForLevel(newLevel);
        int totalSouls = characterEntity.getSoulsBanked() + characterEntity.getSoulsOnPerson();

        if (cost > totalSouls) {
            throw new PlayerCharacterInvalidException((cost - totalSouls) + " more souls required to level up");
        }

        int newBankedSouls = characterEntity.getSoulsBanked() - cost;
        characterEntity.setSoulsBanked(Math.max(0, newBankedSouls));

        // If the banked souls don't fully cover the level up, add the negative banked souls to the souls on person
        if (newBankedSouls < 0) {
            characterEntity.setSoulsOnPerson(characterEntity.getSoulsOnPerson() + newBankedSouls);
        }

        PlayerCharacter playerCharacter = fromEntity(characterEntity);
        int currentPosition = playerCharacter.getPosition();
        int oldMaxPosition = playerCharacter.getMaxPosition();

        characterEntity.setLevel(newLevel);
        characterEntity.setPositionDice(Math.min(newLevel, playerCharacter.getPositionDice() + 1));

        playerCharacter = fromEntity(characterEntity);
        int newMaxPosition = playerCharacter.getMaxPosition();

        characterEntity.setPosition(currentPosition + (newMaxPosition - oldMaxPosition));

        List<CharacterClassFeature> features = playerCharacter.getCharacterClass().getLevelFeatures().get(newLevel);

        if (features != null && !features.isEmpty()) {
            features.forEach(feature -> {
                ClassFeatureEntity featureEntity = new ClassFeatureEntity();

                featureEntity.setCharacter(characterEntity);
                featureEntity.setFeature(feature.getName());

                characterEntity.getClassFeatures().add(featureEntity);
            });
        }

        characterRepository.save(characterEntity);

        return playerCharacter.getCharacterClass().getLevelFeatures().containsKey(newLevel);
    }

    public void modifyClassFeatures(Integer characterId, Map<String, List<String>> valueMap) {
        PlayerCharacterEntity characterEntity = characterRepository.findById(characterId)
                .orElseThrow(PlayerCharacterInvalidException::new);

        valueMap.forEach((featureName, values) -> {
            if (featureName == null) {
                return;
            }

            ClassFeatureEntity thisFeatureEntity = null;
            LinkedList<ClassFeatureOptionEntity> usingOptionEntities = new LinkedList<>();

            for (ClassFeatureEntity featureEntity : characterEntity.getClassFeatures()) {
                if (featureEntity.getFeature().equals(featureName)) {
                    thisFeatureEntity = featureEntity;
                    usingOptionEntities.addAll(featureEntity.getOptions());
                    break;
                }
            }

            if (thisFeatureEntity == null) {
                thisFeatureEntity = new ClassFeatureEntity();
                thisFeatureEntity.setCharacter(characterEntity);
                thisFeatureEntity.setFeature(featureName);
                thisFeatureEntity.setOptions(new LinkedList<>());

                characterEntity.getClassFeatures().add(thisFeatureEntity);
            }

            for (String value : values) {
                if (usingOptionEntities.isEmpty()) {
                    ClassFeatureOptionEntity newOptionEntity = new ClassFeatureOptionEntity();

                    newOptionEntity.setFeature(thisFeatureEntity);
                    newOptionEntity.setOption(value);

                    thisFeatureEntity.getOptions().add(newOptionEntity);
                } else {
                    usingOptionEntities.pollFirst().setOption(value);
                }
            }

            thisFeatureEntity.getOptions().removeIf(usingOptionEntities::contains);
        });

        characterRepository.save(characterEntity);
    }

    public NewPosition modPosition(Integer characterId, Integer position) {
        return modPosition(characterId, position, (characterEntity, character) -> {
            int newPosition = Math.max(Math.min(character.getPosition() + position, character.getMaxPosition()), 0);
            characterEntity.setPosition(newPosition);
        });
    }

    public NewPosition setPosition(Integer characterId, Integer position) {
        return modPosition(characterId, position, (characterEntity, character) -> {
            int newPosition = Math.max(Math.min(position, character.getMaxPosition()), 0);
            characterEntity.setPosition(newPosition);
        });
    }

    public NewPosition startCombat(Integer characterId, Integer position) {
        return modPosition(characterId, position, (characterEntity, character) -> {
            if (characterEntity.getPreCombatMaxPosition() != null || position <= 0) {
                return;
            }

            characterEntity.setPreCombatMaxPosition(character.getMaxPosition());
            characterEntity.setPosition(character.getPosition() + position);

            // Add a class feature for the bonus position
            ClassFeatureEntity combatFeature = new ClassFeatureEntity();

            combatFeature.setCharacter(characterEntity);
            combatFeature.setFeature(COMBAT_FEATURE);

            ClassFeatureOptionEntity combatOption = new ClassFeatureOptionEntity();

            combatOption.setFeature(combatFeature);
            combatOption.setOption(String.valueOf(position));

            combatFeature.setOptions(Collections.singletonList(combatOption));

            characterEntity.getClassFeatures().add(combatFeature);
        });
    }

    public NewPosition stopCombat(Integer characterId) {
        return modPosition(characterId, 0, (characterEntity, character) -> {
            if (characterEntity.getPreCombatMaxPosition() == null) {
                return;
            }

            if (characterEntity.getPreCombatMaxPosition() < characterEntity.getPosition()) {
                characterEntity.setPosition(characterEntity.getPreCombatMaxPosition());
            }

            characterEntity.setPreCombatMaxPosition(null);

            characterEntity.getClassFeatures().removeIf(classFeatureEntity -> classFeatureEntity.getFeature().equals(COMBAT_FEATURE));
        });
    }

    private NewPosition modPosition(Integer characterId, Integer position, BiConsumer<PlayerCharacterEntity, PlayerCharacter> modifyCharacterEntity) {
        if (position == null) {
            throw new PlayerCharacterInvalidException("Position be a number");
        }

        PlayerCharacterEntity characterEntity = characterRepository.findById(characterId)
                .orElseThrow(PlayerCharacterInvalidException::new);
        PlayerCharacter playerCharacter = fromEntity(characterEntity);

        modifyCharacterEntity.accept(characterEntity, playerCharacter);

        characterRepository.save(characterEntity);

        // Re-convert as a change in combat state might have affected current/max position
        playerCharacter = fromEntity(characterEntity);

        return new NewPosition(
                playerCharacter.getPosition(),
                playerCharacter.getMaxPosition(),
                playerCharacter.isBloodied()
        );
    }

    public void die(Integer characterId) {
        PlayerCharacterEntity characterEntity = characterRepository.findById(characterId)
                .orElseThrow(PlayerCharacterInvalidException::new);

        stopCombat(characterId);
        longRest(characterId);

        characterEntity.setSoulsOnPerson(0);

        characterRepository.save(characterEntity);
    }

    public NewPosition shortRest(Integer characterId, Integer position, Integer positionDiceSpent) {
        return modPosition(characterId, position, (characterEntity, character) -> {
            if (positionDiceSpent == null || positionDiceSpent > character.getPositionDice()) {
                throw new PlayerCharacterInvalidException("Not enough position dice");
            }

            int newPosition = Math.max(Math.min(character.getPosition() + position, character.getMaxPosition()), 0);
            characterEntity.setPosition(newPosition);
            characterEntity.setPositionDice(character.getPositionDice() - positionDiceSpent);

            notifyFeaturesOfRest(characterEntity, RestType.SHORT);
        });
    }

    public NewPosition longRest(Integer characterId) {
        return modPosition(characterId, 0, (characterEntity, character) -> {
            // Fully restore position
            characterEntity.setPosition(character.getMaxPosition());

            // Restore position dice equal to half of the expended dice
            characterEntity.setPositionDice(Math.min(character.getLevel(), character.getPositionDice() + (character.getLevel() / 2)));

            notifyFeaturesOfRest(characterEntity, RestType.LONG);

            // Restore spell casts
            for (SpellEntity spellEntity : characterEntity.getSpells()) {
                spellEntity.setSpentCasts(0);
            }
        });
    }

    private void notifyFeaturesOfRest(PlayerCharacterEntity characterEntity, RestType restType) {
        for (ClassFeatureEntity classFeature : characterEntity.getClassFeatures()) {
            characterClassFactory.getFeatureByName(classFeature.getFeature()).ifPresent(feature -> {
                boolean rested = feature.getEffects().stream().anyMatch(effect -> effect.onRest(restType));

                if (rested) {
                    characterEntity.getSpentUses().put(feature.getName(), 0);
                }
            });

            for (ClassFeatureOptionEntity option : classFeature.getOptions()) {
                characterClassFactory.getFeatureOptionByName(classFeature.getFeature(), option.getOption()).ifPresent(feature -> {
                    boolean rested = feature.getEffects().stream().anyMatch(effect -> effect.onRest(restType));

                    if (rested) {
                        characterEntity.getSpentUses().put(feature.getName(), 0);
                    }
                });
            }
        }

        for (InventoryEntity inventoryEntity : characterEntity.getInventory()) {
            itemFactory.getByName(inventoryEntity.getItem()).ifPresent(item -> {
                boolean rested = item.getEffects().stream().anyMatch(effect -> effect.onRest(restType));

                if (rested) {
                    characterEntity.getSpentUses().put(item.getName(), 0);
                }
            });
        }
    }

    public int addSoulsOnPerson(Integer characterId, Integer souls) {
        if (souls == null) {
            throw new PlayerCharacterInvalidException("Souls must be a number");
        }

        PlayerCharacterEntity characterEntity = characterRepository.findById(characterId)
                .orElseThrow(PlayerCharacterInvalidException::new);
        int newSouls = souls;

        if (characterEntity.getSoulsOnPerson() != null) {
            newSouls += characterEntity.getSoulsOnPerson();
        }

        characterEntity.setSoulsOnPerson(newSouls);
        return newSouls;
    }

    public int moveSoulsToBank(Integer characterId, Integer souls) {
        if (souls == null || souls <= 0) {
            throw new PlayerCharacterInvalidException("Souls be a number");
        }

        PlayerCharacterEntity characterEntity = characterRepository.findById(characterId)
                .orElseThrow(PlayerCharacterInvalidException::new);

        if (characterEntity.getSoulsOnPerson() < souls) {
            throw new PlayerCharacterInvalidException("Not enough souls on person");
        }

        characterEntity.setSoulsOnPerson(characterEntity.getSoulsOnPerson() - souls);

        int newSouls = souls;

        if (characterEntity.getSoulsOnPerson() != null) {
            newSouls += characterEntity.getSoulsBanked();
        }

        characterEntity.setSoulsBanked(newSouls);
        return newSouls;
    }

    public int addItem(Integer characterId, String itemName, Integer amount, InventorySlot slot) {
        PlayerCharacterEntity characterEntity = characterRepository.findById(characterId)
                .orElseThrow(PlayerCharacterInvalidException::new);

        if (characterEntity.getInventory().size() >= 100) {
            throw new PlayerCharacterInvalidException("Inventory too large!");
        }

        Item item = itemFactory.getByName(itemName)
                .orElseThrow(ItemInvalidException::new);

        unequipOtherItems(characterEntity.getInventory(), slot);

        InventoryEntity inventoryEntity = new InventoryEntity();

        inventoryEntity.setCharacter(characterEntity);
        inventoryEntity.setItem(item.getName());
        inventoryEntity.setActiveSlot(slot);

        if (amount != null) {
            inventoryEntity.setAmount(amount);
        }

        inventoryEntity = inventoryRepository.save(inventoryEntity);
        return inventoryEntity.getId();
    }

    public void removeItem(Integer inventoryItemId) {
        if (inventoryItemId == null) {
            throw new ItemInvalidException();
        }

        inventoryRepository.deleteById(inventoryItemId);
    }

    public void setItemAmount(Integer inventoryItemId, Integer amount) {
        InventoryEntity inventoryEntity = inventoryRepository.findById(inventoryItemId)
                .orElseThrow(ItemInvalidException::new);

        if (amount == null || amount < 0) {
            throw new ItemInvalidException("Amount must be a positive number");
        }

        inventoryEntity.setAmount(amount);

        inventoryRepository.save(inventoryEntity);
    }

    public void equipItem(Integer characterId, Integer inventoryItemId, InventorySlot slot) {
        PlayerCharacterEntity characterEntity = characterRepository.findById(characterId)
                .orElseThrow(PlayerCharacterInvalidException::new);
        InventoryEntity inventoryEntity = inventoryRepository.findById(inventoryItemId)
                .orElseThrow(ItemInvalidException::new);

        unequipOtherItems(characterEntity.getInventory(), slot);

        inventoryEntity.setActiveSlot(slot);

        inventoryRepository.save(inventoryEntity);
    }

    private void unequipOtherItems(List<InventoryEntity> items, InventorySlot slot) {
        if (slot != null) {
            for (InventoryEntity otherItem : items) {
                if (otherItem.getActiveSlot() == slot) {
                    otherItem.setActiveSlot(null);
                    inventoryRepository.save(otherItem);
                }
            }
        }
    }

    public void setSpentUses(Integer characterId, String key, Integer uses) {
        PlayerCharacterEntity characterEntity = characterRepository.findById(characterId)
                .orElseThrow(PlayerCharacterInvalidException::new);

        if (key == null) {
            throw new PlayerCharacterInvalidException("Uses key must not be null");
        } else if (uses == null || uses < 0) {
            throw new PlayerCharacterInvalidException("Usages must be a positive number");
        }

        characterEntity.getSpentUses().put(key, uses);

        characterRepository.save(characterEntity);
    }

    public void learnSpell(Integer characterId, String spellName, boolean innate) {
        PlayerCharacterEntity characterEntity = characterRepository.findById(characterId)
                .orElseThrow(PlayerCharacterInvalidException::new);
        Spell spell = spellFactory.getByName(spellName)
                .orElseThrow(SpellInvalidException::new);

        // Only check if we can learn the spell if it's not an innate one. This is a slight hack to get around fromEntity
        // causing NPEs because it expects the entity to have been saved after setClass at least once because calling it.
        if (!innate) {
            PlayerCharacter playerCharacter = fromEntity(characterEntity);
            CharacterClass characterClass = playerCharacter.getCharacterClass();

            if (!characterClass.isSpellCaster() || characterClass.getSpellCaster().getSpellFamily() != spell.getFamily()) {
                throw new SpellInvalidException("Character is not a spell caster or cannot learn spells from this family");
            }
        }

        spellRepository.findByCharacterAndSpell(characterEntity, spell.getName()).ifPresent(spellEntity -> {
            throw new SpellInvalidException("Spell already learnt");
        });

        SpellEntity spellEntity = new SpellEntity();

        spellEntity.setSpell(spell.getName());
        spellEntity.setCharacter(characterEntity);
        spellEntity.setAttuned(innate);

        characterEntity.getSpells().add(spellEntity);

        characterRepository.save(characterEntity);
    }

    public void forgetSpell(Integer characterId, String spellName) {
        PlayerCharacterEntity characterEntity = characterRepository.findById(characterId)
                .orElseThrow(PlayerCharacterInvalidException::new);

        characterEntity.getSpells().removeIf(spellEntity -> spellEntity.getSpell().equals(spellName));

        characterRepository.save(characterEntity);
    }

    public void attuneSpell(Integer characterId, String spellName) {
        PlayerCharacterEntity characterEntity = characterRepository.findById(characterId)
                .orElseThrow(PlayerCharacterInvalidException::new);
        Spell spell = spellFactory.getByName(spellName)
                .orElseThrow(SpellInvalidException::new);
        SpellEntity spellEntity = spellRepository.findByCharacterAndSpell(characterEntity, spell.getName())
                .orElseThrow(() -> new SpellInvalidException("Spell not learnt"));

        if (spellEntity.getAttuned()) {
            throw new SpellInvalidException("Already attuned");
        }

        if (characterEntity.getLevel() < spell.getLevel()) {
            throw new SpellInvalidException("Level " + spell.getLevel() + " is required to attune this spell");
        }

        PlayerCharacter playerCharacter = fromEntity(characterEntity);
        int usedAttunementSlots = playerCharacter.getUsedAttunementSlots();
        int maxAttunementSlots = playerCharacter.getMaxAttunementSlots();

        if (usedAttunementSlots + spell.getSlots() > maxAttunementSlots) {
            throw new SpellInvalidException("Not enough attunement slots");
        }

        spellEntity.setAttuned(true);

        spellRepository.save(spellEntity);
    }

    public void unattuneSpell(Integer characterId, String spellName) {
        SpellEntity spellEntity = spellRepository.findByCharacterIdAndSpell(characterId, spellName)
                .orElseThrow(() -> new SpellInvalidException("Spell not learnt"));

        spellEntity.setAttuned(false);

        spellRepository.save(spellEntity);
    }

    public void setSpellCasts(Integer characterId, String spellName, Integer spentCasts) {
        SpellEntity spellEntity = spellRepository.findByCharacterIdAndSpell(characterId, spellName)
                .orElseThrow(() -> new SpellInvalidException("Spell not learnt"));

        if (spentCasts == null || spentCasts < 0) {
            spentCasts = 0;
        }

        spellEntity.setSpentCasts(spentCasts);

        spellRepository.save(spellEntity);
    }

    public PlayerCharacter fromEntity(PlayerCharacterEntity characterEntity) {
        return conversionService.convert(characterEntity, PlayerCharacter.class);
    }
}
