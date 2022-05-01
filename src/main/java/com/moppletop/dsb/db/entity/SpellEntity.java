package com.moppletop.dsb.db.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.*;

@Entity
@Table(
        name = "character_spell"
)
@Data
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "spell"})
public class SpellEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "character_spell_sequence_generator")
    @GenericGenerator(
            name = "character_spell_sequence_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "character_spell_sequence")
            }
    )
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "character_id", nullable = false)
    private PlayerCharacterEntity character;

    @Column(name = "spell", length = 32, nullable = false)
    private String spell;

    @Column(name = "attuned", nullable = false)
    private Boolean attuned = false;

    @Column(name = "spent_casts", nullable = false)
    private Integer spentCasts = 0;

}
