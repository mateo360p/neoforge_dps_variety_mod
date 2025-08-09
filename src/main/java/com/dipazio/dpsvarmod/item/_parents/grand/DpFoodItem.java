package com.dipazio.dpsvarmod.item._parents.grand;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;

public class DpFoodItem extends DpItem {
    public DpFoodItem(int nutrition, float saturationModifier, Properties properties) {
        super(properties.food(
            new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturationModifier)
                .build())
        );
    }

    public DpFoodItem(int nutrition, float saturationModifier, Properties properties, Consumable effect) {
        super(properties.food(
            new FoodProperties.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturationModifier)
                .build(),
            effect)
        );
    }
}
