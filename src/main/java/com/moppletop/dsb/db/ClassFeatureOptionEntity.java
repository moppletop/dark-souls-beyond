package com.moppletop.dsb.db;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.*;

@Entity
@Table(
        name = "character_class_feature_option"
)
@Data
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "option"})
public class ClassFeatureOptionEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "character_class_feature_option_sequence_generator")
    @GenericGenerator(
            name = "character_class_feature_option_sequence_generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "character_class_feature_option_sequence")
            }
    )
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "feature_id", nullable = false)
    private ClassFeatureEntity feature;

    @Column(name = "option", length = 32, nullable = false)
    private String option;

}
