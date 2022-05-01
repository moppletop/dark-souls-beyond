package com.moppletop.dsb.domain.character;

import com.moppletop.dsb.domain.spell.Spell;
import com.moppletop.dsb.system.effect.Effect;
import com.moppletop.dsb.system.effect.Item;
import com.moppletop.dsb.system.effect.effect.EffectSource;
import com.moppletop.dsb.domain.game.Ability;
import com.moppletop.dsb.domain.game.ActionType;
import com.moppletop.dsb.domain.game.Calculations;
import com.moppletop.dsb.domain.game.DamageType;
import com.moppletop.dsb.domain.game.Dice;
import com.moppletop.dsb.domain.game.Resistance;
import com.moppletop.dsb.domain.game.ResistanceType;
import com.moppletop.dsb.domain.game.RollType;
import com.moppletop.dsb.domain.game.Skill;
import com.moppletop.dsb.domain.game.WeaponProperty;
import com.moppletop.dsb.exception.PlayerCharacterInvalidException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@EqualsAndHashCode(of = "id")
public class PlayerCharacter {

    @Getter
    private final int id;
    @Getter
    private final int userId;

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private CharacterClass characterClass;
    @Getter
    @Setter
    private int level;

    @Getter
    private final List<CharacterClassFeature> classFeaturesAndOptions = new LinkedList<>();
    @Getter
    private final Map<CharacterClassFeature, List<CharacterClassFeature>> classFeatureOptions = new HashMap<>();

    @Getter
    private final Map<Ability, Integer> abilityScoreBase = new EnumMap<>(Ability.class);
    @Getter
    private final Map<Ability, List<Modifier>> abilityScoreModifiers = new EnumMap<>(Ability.class);
    @Getter
    private final Map<Ability, List<Modifier>> savingThrowModifiers = new EnumMap<>(Ability.class);

    @Getter
    private final Map<Skill, List<Modifier>> skillModifiers = new EnumMap<>(Skill.class);
    @Getter
    private final Map<Skill, List<Modifier>> passiveScoreModifiers = new EnumMap<>(Skill.class);
    @Getter
    private final List<String> additionalSenses = new ArrayList<>();

    @Getter
    private final List<Modifier> maxPositionModifiers = new ArrayList<>();
    @Getter
    private final List<Modifier> armourClassModifiers = new ArrayList<>();
    @Getter
    private final List<Modifier> speedModifiers = new ArrayList<>();
    @Getter
    private final List<Modifier> initiativeModifiers = new ArrayList<>();

    @Getter
    private final List<Resistance> resistances = new ArrayList<>();

    @Getter
    @Setter
    private int position;
    @Getter
    @Setter
    private boolean inCombat;
    @Getter
    @Setter
    private int positionDice;

    @Getter
    @Setter
    private int soulsOnPerson;
    @Getter
    @Setter
    private int soulsBanked;

    @Getter
    private final List<InventoryItem> inventory = new LinkedList<>();

    @Getter
    private final Map<String, Integer> spentUses = new HashMap<>();

    @Getter
    private final List<LearntSpell> learntSpells = new LinkedList<>();

    @Getter
    private final List<WeaponAction> weaponActions = new LinkedList<>();
    @Getter
    private final List<AbilityAction> abilityActions = new LinkedList<>();

    public PlayerCharacter(int id, int userId) {
        this.id = id;
        this.userId = userId;

        initialiseCollections();
    }

    private void initialiseCollections() {
        for (Ability ability : Ability.values()) {
            abilityScoreModifiers.put(ability, new ArrayList<>());
            savingThrowModifiers.put(ability, new ArrayList<>());
        }

        passiveScoreModifiers.put(Skill.INSIGHT, new ArrayList<>());
        passiveScoreModifiers.put(Skill.INVESTIGATION, new ArrayList<>());
        passiveScoreModifiers.put(Skill.PERCEPTION, new ArrayList<>());

        for (Skill skill : Skill.values()) {
            skillModifiers.put(skill, new ArrayList<>());
        }
    }

    /*
        Level
     */

    public int getProficiencyBonus() {
        return Calculations.getProficiencyBonus(level);
    }

    /*
        Ability scores & saving throws
     */

    public void setBaseAbilityScores(Map<Ability, Integer> abilityScores) {
        this.abilityScoreBase.putAll(abilityScores);

        abilityScores.forEach((ability, score) -> modifyAbilityScore("Base", ability, score));
    }

    public void modifyAbilityScore(String source, Ability ability, int mod) {
        Modifier modifier = new Modifier(source, mod);

        abilityScoreModifiers.get(ability).add(modifier);
    }

    public void modifyAbilityScore(String source, Ability ability, RollType rollType) {
        abilityScoreModifiers.get(ability).add(new Modifier(source, rollType));
    }

    public void addProficientSavingThrow(String source, Ability savingThrowAbility) {
        savingThrowModifiers.get(savingThrowAbility).add(new Modifier(source, getProficiencyBonus()));
    }

    public void modifySavingThrow(String source, Ability savingThrowAbility, RollType rollType) {
        savingThrowModifiers.get(savingThrowAbility).add(new Modifier(source, rollType));
    }

    public int getAbilityScore(Ability score) {
        return abilityScoreModifiers.get(score).stream()
                .mapToInt(Modifier::getMod)
                .sum();
    }

    public Map<Ability, Modification> getAbilityScores() {
        return getModifications(abilityScoreModifiers);
    }

    public Map<Ability, Modification> getSavingThrows() {
        return getModifications(savingThrowModifiers);
    }

    /*
        Skills
     */

    public void modifySkill(String source, Skill skill, int mod) {
        skillModifiers.get(skill).add(new Modifier(source, mod));
    }

    public void addProficientSavingThrow(String source, Skill skill) {
        modifySkill(source, skill, Calculations.getProficiencyBonus(level));
    }

    public void modifySavingThrow(String source, Skill skill, RollType rollType) {
        skillModifiers.get(skill).add(new Modifier(source, rollType));
    }

    public Map<Skill, Modification> getSkills() {
        return getModifications(skillModifiers);
    }

    public Map<Skill, Modification> getPassiveScores() {
        return getModifications(passiveScoreModifiers);
    }

    /*
        Additional Senses
     */

    public void addAdditionalSense(String sense) {
        additionalSenses.add(sense);
    }

    /*
        Max position
     */

    public void modifyMaxPosition(String source, int mod) {
        maxPositionModifiers.add(new Modifier(source, mod));
    }

    public int getMaxPosition() {
        return maxPositionModifiers.stream()
                .mapToInt(Modifier::getMod)
                .sum();
    }

    public boolean isBloodied() {
        return isInCombat() && getPosition() <= getMaxPosition() / 2;
    }

    /*
        Armour Class
     */

    public void modifyArmourClass(String source, int mod) {
        armourClassModifiers.add(new Modifier(source, mod));
    }

    public int getArmourClass() {
        return armourClassModifiers.stream()
                .mapToInt(Modifier::getMod)
                .sum();
    }

    /*
        Speed
     */

    public void modifySpeed(String source, int mod) {
        speedModifiers.add(new Modifier(source, mod));
    }

    public int getSpeed() {
        return speedModifiers.stream()
                .mapToInt(Modifier::getMod)
                .sum();
    }

    /*
        Initiative
     */

    public void modifyInitiative(String source, int mod) {
        initiativeModifiers.add(new Modifier(source, mod));
    }

    public int getInitiative() {
        return initiativeModifiers.stream()
                .mapToInt(Modifier::getMod)
                .sum();
    }

    /*
        Resistance
     */

    public void addResistance(String source, String to, ResistanceType resistanceType) {
        resistances.add(new Resistance(source, to, resistanceType));
    }

    public void derive() {
        Map<Ability, Modification> abilityScores = getAbilityScores();

        abilityScores.forEach((ability, modification) -> {
            int bonus = Calculations.getAbilityScoreModifier(modification.getMod());

            if (bonus == 0) {
                return;
            }

            Modifier modifier = new Modifier(ability.getName() + " Ability Score", bonus);

            savingThrowModifiers.get(ability).add(modifier);

            for (Skill skill : Skill.values()) {
                if (skill.getAbility() == ability) {
                    skillModifiers.get(skill).add(modifier);

                    List<Modifier> passiveModifiers = passiveScoreModifiers.get(skill);

                    if (passiveModifiers != null) {
                        passiveModifiers.add(modifier);
                    }
                }
            }

            if (ability == Ability.DEXTERITY) {
                initiativeModifiers.add(modifier);
            }
        });
    }

    private <K extends Comparable<K>> Map<K, Modification> getModifications(Map<K, List<Modifier>> map) {
        Map<K, Modification> modifications = new TreeMap<>();

        map.forEach((k, modifiers) -> {
            int score = 0;
            RollType rollType = null;

            for (Modifier modifier : modifiers) {
                score += modifier.getMod();

                // Ensure that advantage and disadvantage cancel each other out
                if (modifier.getRollType() != null) {
                    if (rollType != null && rollType != modifier.getRollType()) {
                        rollType = null;
                        break;
                    }

                    rollType = modifier.getRollType();
                }
            }

            modifications.put(k, new Modification(score, rollType));
        });

        return modifications;
    }

    /*
        Spells
     */

    public void learnSpell(Spell spell, int spentCasts, boolean attuned) {
        learntSpells.add(new LearntSpell(spell, spentCasts, attuned));
    }

    public int getUsedAttunementSlots() {
        return learntSpells.stream()
                .filter(LearntSpell::isAttuned)
                .mapToInt(spell -> spell.getSpell().getSlots())
                .sum();
    }

    public int getMaxAttunementSlots() {
        if (characterClass.isSpellCaster() && characterClass.getSpecialSlots() != null) {
            return characterClass.getSpecialSlots().getLevels().get(level - 1);
        }

        return 0;
    }

    public boolean canLearn(Spell spell) {
        if (!characterClass.isSpellCaster() || characterClass.getSpellCaster().getSpellFamily() != spell.getFamily() || level < spell.getLevel()) {
            return false;
        }

        for (LearntSpell learntSpell : learntSpells) {
            if (learntSpell.getSpell().equals(spell)) {
                return false;
            }
        }

        return true;
    }

    public int getSpellSaveDC() {
        if (!characterClass.isSpellCaster()) {
            return -1;
        }

        return 8 + getProficiencyBonus() + Calculations.getAbilityScoreModifier(getAbilityScore(characterClass.getSpellCaster().getAbility()));
    }

    /*
        Actions
     */

    public void addWeaponAction(Item weapon, int baseRange, int maxRange, int hitBonus, int damageDiceCount, Dice damageDice, int flatDamage, DamageType damageType, WeaponProperty[] properties) {
        String rangeDescription;

        if (baseRange == maxRange) {
            rangeDescription = baseRange + " ft.";
        } else {
            rangeDescription = baseRange + "/" + maxRange;
        }

        WeaponAction weaponAction = new WeaponAction(
                weapon,
                ActionType.ACTION,
                rangeDescription,
                hitBonus,
                damageDiceCount,
                damageDice,
                flatDamage,
                damageType,
                Arrays.stream(properties)
                        .map(WeaponProperty::getName)
                        .collect(Collectors.joining(", "))
        );

        for (CharacterClassFeature feature : classFeaturesAndOptions) {
            for (Effect effect : feature.getEffects()) {
                effect.onWeaponAction(this, weaponAction);
            }
        }

        weaponActions.add(weaponAction);
    }

    public void addAbility(ActionType actionType, EffectSource source, String description, Integer uses) {
        AbilityAction abilityAction = new AbilityAction(
                source.getName(),
                actionType,
                source,
                description,
                uses
        );

        for (CharacterClassFeature feature : classFeaturesAndOptions) {
            for (Effect effect : feature.getEffects()) {
                effect.onAbilityAction(this, source, abilityAction);
            }
        }

        abilityActions.add(abilityAction);
    }

    /*
        Extra
     */

    public void assertComplete() {
        if (characterClass == null) {
            throw new PlayerCharacterInvalidException("Player character is not complete");
        }
    }
}
