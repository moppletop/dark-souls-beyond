package com.moppletop.dsb.item;

import com.moppletop.dsb.DarkSoulsBeyondApplication;
import com.moppletop.dsb.factory.ItemFactory;
import com.moppletop.dsb.util.SampleData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "dsb.test.sample_size=10"
        }
)
public class ItemTests {

    @Autowired ItemFactory itemFactory;

    @Autowired
    SampleData sampleData;

    @SpringBootConfiguration
    @ComponentScan(basePackageClasses = DarkSoulsBeyondApplication.class)
    public static class Config {
    }


    @Test
    void testAbilityRequirement() {
//        Item catarinaArmour = itemFactory.getItemsByName().get("Catarina Armour");
//
//        assertThat(catarinaArmour).isNotNull();
//
//        PlayerCharacter builder = sampleData.getRandomPlayerCharacter();
//
//        builder.getAbilityScores().put(AbilityScore.STRENGTH, 15);
//
//        assertThat(catarinaArmour.canEquip(builder)).isTrue();
//
//        builder.getAbilityScores().put(AbilityScore.STRENGTH, 14);
//
//        assertThat(catarinaArmour.canEquip(builder)).isFalse();
    }

}
