package com.moppletop.dsb.db.entity;

import com.moppletop.dsb.domain.game.InventorySlot;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(
        name = "character_inventory"
)
@Data
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "item"})
public class InventoryEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "character_inventory_sequence_generator")
    @GenericGenerator(
            name = "character_inventory_sequence_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "character_inventory_sequence")
            }
    )
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "character_id", nullable = false)
    private PlayerCharacterEntity character;

    @Column(name = "item", length = 32, nullable = false)
    private String item;

    @Column(name = "amount", nullable = false)
    private Integer amount = 1;

    @Column(name = "starting_item", nullable = false)
    private Boolean startingItem = false;

    @Column(name = "active_slot")
    @Enumerated(EnumType.ORDINAL)
    private InventorySlot activeSlot;

}
