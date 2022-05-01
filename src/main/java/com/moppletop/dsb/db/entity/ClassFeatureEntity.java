package com.moppletop.dsb.db.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(
        name = "character_class_feature"
)
@Data
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "feature"})
public class ClassFeatureEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "character_class_feature_sequence_generator")
    @GenericGenerator(
            name = "character_class_feature_sequence_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "character_class_feature_sequence")
            }
    )
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "character_id", nullable = false)
    private PlayerCharacterEntity character;

    @Column(name = "feature", length = 48, nullable = false)
    private String feature;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassFeatureOptionEntity> options;

}
