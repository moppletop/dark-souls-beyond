package com.moppletop.dsb.domain.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Map;

@Entity
@Table(
        name = "character"
)
@Data
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "name"})
public class PlayerCharacterEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "player_character_sequence_generator")
    @GenericGenerator(
            name = "player_character_sequence_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "character_sequence")
            }
    )
    private Integer id;

    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "class")
    private Integer classId;

    @Column(name = "origin")
    private Integer originId;

    @Column(name = "level", nullable = false)
    private Integer level = 1;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassFeatureEntity> classFeatures;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpellEntity> spells;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryEntity> inventory;

    @ElementCollection
    @CollectionTable(
            name = "character_uses",
            joinColumns = {
                    @JoinColumn(name = "character_id", referencedColumnName = "id")
            }
    )
    @MapKeyColumn(name = "key", length = 32)
    @Column(name = "uses")
    private Map<String, Integer> spentUses;

    @Column(name = "position")
    private Integer position;

    @Column(name = "pre_combat_max_position")
    private Integer preCombatMaxPosition;

    @Column(name = "position_dice", nullable = false)
    private Integer positionDice = 1;

    @Column(name = "souls_on_person", nullable = false)
    private Integer soulsOnPerson = 0;

    @Column(name = "souls_banked", nullable = false)
    private Integer soulsBanked = 0;

}
