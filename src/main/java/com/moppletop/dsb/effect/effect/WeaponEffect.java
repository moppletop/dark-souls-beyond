package com.moppletop.dsb.effect.effect;

import com.fasterxml.jackson.databind.JsonNode;
import com.moppletop.dsb.game.Ability;
import com.moppletop.dsb.game.InventorySlot;
import com.moppletop.dsb.character.PlayerCharacter;
import com.moppletop.dsb.effect.Effect;
import com.moppletop.dsb.effect.EffectKey;
import com.moppletop.dsb.effect.EffectSource;
import com.moppletop.dsb.game.Calculations;
import com.moppletop.dsb.game.DamageType;
import com.moppletop.dsb.game.Dice;
import com.moppletop.dsb.game.WeaponProperty;
import com.moppletop.dsb.item.InventorySource;

import java.util.Map;

@EffectKey("WEAPON")
public class WeaponEffect extends Effect {

    private final Dice damageDice;
    private final int damageDiceCount;
    private final DamageType damageType;
    private final WeaponProperty[] properties;

    public WeaponEffect(Map<String, JsonNode> parameters) {
        super(parameters);

        this.damageDice = getEnum("damageDice", Dice::valueOf);

        JsonNode damageDiceCountNode = parameters.get("damageDiceCount");
        this.damageDiceCount = damageDiceCountNode == null ? 1 : damageDiceCountNode.asInt(1);

        this.damageType = getEnum("damageType", DamageType::valueOf);

        JsonNode propertyNodes = parameters.get("properties");

        if (propertyNodes == null || !propertyNodes.isArray()) {
            properties = new WeaponProperty[0];
        } else {
            this.properties = new WeaponProperty[propertyNodes.size()];

            for (int i = 0; i < properties.length; i++) {
                JsonNode node = propertyNodes.get(i);
                WeaponProperty property = WeaponProperty.valueOf(node.asText());
                properties[i] = property;
            }
        }
    }

    @Override
    public void onPostApply(PlayerCharacter character, EffectSource source) {
        InventorySource inventorySource = (InventorySource) source;
        int primaryAbilityScore = character.getAbilityScore(Ability.STRENGTH);
        int baseRange = 5, maxRange = 5;
        Dice damageDice = this.damageDice;

        for (WeaponProperty property : properties) {
            switch (property) {
                case FINESSE:
                    primaryAbilityScore = Math.max(primaryAbilityScore, character.getAbilityScore(Ability.DEXTERITY));
                    break;
                case REACH:
                    baseRange += 5;
                    maxRange += 5;
                    break;
                case VERSATILE:
                    if (inventorySource.getSlot().isTwoHanded()) {
                        damageDice = Dice.values()[damageDice.ordinal() + 1];
                    }

                    break;
                case TWO_HANDED:
                    if (inventorySource.getSlot() != InventorySlot.LEFT_HAND_WEAPON) {
                        return;
                    }
            }
        }

        primaryAbilityScore = Calculations.getAbilityScoreModifier(primaryAbilityScore);
        int hitBonus = primaryAbilityScore + character.getProficiencyBonus();

        character.addWeaponAction(inventorySource.getItem(), baseRange, maxRange, hitBonus, damageDiceCount, damageDice, primaryAbilityScore, damageType, properties);
    }

    @Override
    protected String getDefaultDescription() {
        StringBuilder builder = new StringBuilder(32);

        builder.append(damageDice.getExpression(damageDiceCount))
                .append(' ')
                .append(damageType.getName());

        for (WeaponProperty property : properties) {
            builder.append(", ").append(property.getName());
        }

        return builder.toString();
    }
}
