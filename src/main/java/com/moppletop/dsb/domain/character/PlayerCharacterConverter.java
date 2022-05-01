package com.moppletop.dsb.domain.character;

import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.system.effect.Item;
import com.moppletop.dsb.domain.game.Ability;
import com.moppletop.dsb.domain.game.ActionType;
import com.moppletop.dsb.domain.game.Calculations;
import com.moppletop.dsb.domain.game.InventorySlot;
import com.moppletop.dsb.domain.game.TwoWeaponFighting;
import com.moppletop.dsb.domain.item.InventorySource;
import com.moppletop.dsb.db.entity.ClassFeatureEntity;
import com.moppletop.dsb.db.entity.ClassFeatureOptionEntity;
import com.moppletop.dsb.db.entity.InventoryEntity;
import com.moppletop.dsb.db.entity.PlayerCharacterEntity;
import com.moppletop.dsb.db.entity.SpellEntity;
import com.moppletop.dsb.exception.PlayerCharacterInvalidException;
import com.moppletop.dsb.factory.ItemFactory;
import com.moppletop.dsb.factory.OriginFactory;
import com.moppletop.dsb.factory.SpellFactory;
import com.moppletop.dsb.system.player.PlayerCharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlayerCharacterConverter implements Converter<PlayerCharacterEntity, PlayerCharacter> {

    private final CharacterClassFactory characterClassFactory;
    private final OriginFactory originFactory;
    private final ItemFactory itemFactory;
    private final SpellFactory spellFactory;

    @Override
    public PlayerCharacter convert(PlayerCharacterEntity entity) {
        PlayerCharacter character = new PlayerCharacter(entity.getId(), entity.getUser().getId());

        character.setName(entity.getName());
        character.setLevel(entity.getLevel());

        CharacterClass characterClass = null;
        int level = character.getLevel();

        if (entity.getClassId() != null) {
            characterClass = characterClassFactory.getById(entity.getClassId())
                    .orElseThrow(() -> new PlayerCharacterInvalidException("Class with id " + entity.getClassId() + " does not exist!"));

            character.setCharacterClass(characterClass);
        }

        Origin origin = null;
        if (entity.getOriginId() != null) {
            origin = originFactory.getById(entity.getOriginId())
                    .orElseThrow(() -> new PlayerCharacterInvalidException("Origin with id " + entity.getOriginId() + " does not exist!"));

            character.setOrigin(origin);
        }

        if (entity.getPosition() != null) {
            character.setPosition(entity.getPosition());
        }

        if (entity.getPreCombatMaxPosition() != null) {
            character.setInCombat(true);
        }

        character.setPositionDice(entity.getPositionDice());
        character.setSoulsOnPerson(entity.getSoulsOnPerson());
        character.setSoulsBanked(entity.getSoulsBanked());

        if (characterClass != null) {
            // Apply class features
            for (ClassFeatureEntity classFeature : entity.getClassFeatures()) {

                // Apply combat temporary position
                if (classFeature.getFeature().equals(PlayerCharacterService.COMBAT_FEATURE)) {
                    character.modifyMaxPosition(classFeature.getFeature(), Integer.parseInt(classFeature.getOptions().get(0).getOption()));
                    continue;
                }

                Optional<CharacterClassFeature> optFeature = characterClassFactory.getFeatureByName(classFeature.getFeature());

                if (optFeature.isEmpty()) {
                    continue;
                }

                CharacterClassFeature feature = optFeature.get();

                character.getClassFeaturesAndOptions().add(feature);

                for (ClassFeatureOptionEntity option : classFeature.getOptions()) {
                    Optional<CharacterClassFeature> optFeatureOption = characterClassFactory.getFeatureOptionByName(classFeature.getFeature(), option.getOption());

                    if (optFeatureOption.isPresent()) {
                        CharacterClassFeature featureOption = optFeatureOption.get();

                        character.getClassFeaturesAndOptions().add(featureOption);
                        character.getClassFeatureOptions().computeIfAbsent(feature, k -> new ArrayList<>()).add(featureOption);
                    }
                }
            }

            for (CharacterClassFeature feature : character.getClassFeaturesAndOptions()) {
                for (Effect effect : feature.getEffects()) {
                    effect.onApply(character, feature);
                }
            }
        }

        // Inventory
        for (InventoryEntity inventory : entity.getInventory()) {
            Optional<Item> optItem = itemFactory.getByName(inventory.getItem());

            if (optItem.isEmpty()) {
                continue;
            }

            Item item = optItem.get();
            InventorySlot activeSlot = inventory.getActiveSlot();

            character.getInventory().add(new InventoryItem(inventory.getId(), item, inventory.getAmount(), activeSlot));

            if (activeSlot != null && item.canEquip(character)) {
                InventorySource inventorySource = new InventorySource(item, activeSlot);
                item.getEffects().forEach(effect -> effect.onApply(character, inventorySource));
            }
        }

        // Estus
        Item estusFlask = itemFactory.getEstusFlask();
        for (Effect effect : estusFlask.getEffects()) {
            effect.onApply(character, estusFlask);
        }

        character.getSpentUses().putAll(entity.getSpentUses());

        // Spells
        for (SpellEntity spell : entity.getSpells()) {
            spellFactory.getByName(spell.getSpell()).ifPresent(learntSpell -> character.learnSpell(learntSpell, spell.getSpentCasts(), spell.getAttuned()));
        }

        // Give base position
        if (origin != null) {
            int maxPosition = Calculations.getMaxPosition(origin.getPositionDice(), level, character.getAbilityScore(Ability.CONSTITUTION));

            character.modifyMaxPosition("Level " + level + " with origin (" + origin.getName() + ")", maxPosition);
        }

        // Set max health if none
        if (entity.getPosition() == null) {
            character.setPosition(character.getMaxPosition());
        }

        // Bloodied effects
        if (origin != null && character.isBloodied()) {
            for (Effect effect : origin.getBloodiedEffects()) {
                effect.onApply(character, origin);
                effect.onPostApply(character, origin);
            }
        }

        // Post apply
        for (CharacterClassFeature feature : character.getClassFeaturesAndOptions()) {
            for (Effect effect : feature.getEffects()) {
                effect.onPostApply(character, feature);
            }
        }

        for (InventoryItem inventoryItem : character.getInventory()) {
            Item item = inventoryItem.getItem();

            if (inventoryItem.getSlot() == null || !item.canEquip(character)) {
                continue;
            }

            InventorySource inventorySource = new InventorySource(item, inventoryItem.getSlot());

            for (Effect effect : item.getEffects()) {
                effect.onPostApply(character, inventorySource);
            }
        }

        if (character.getArmourClassModifiers().isEmpty()) {
            character.modifyArmourClass("No Armour", 10);
        }

        // TODO fix
        if (origin != null) {
            character.addAbility(ActionType.BONUS, () -> "Two Weapon Fighting", TwoWeaponFighting.DESCRIPTION, null);
        }

        character.derive();

        return character;
    }
}
