package com.dipazio.dpsvarmod.item._parents.grand;

import com.dipazio.dpsvarmod.item._material.DpToolMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;
import org.jetbrains.annotations.Nullable;

public class DpItem<T extends DpItem<T>> extends Item {
    public int burnTime = -1; // If it's negative then it isn't a fuel
    public DpToolMaterial material;

    public DpItem(Properties properties) {
        super(properties);
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType, FuelValues fuelValues) {
        if (burnTime >= 0) return burnTime;
        return super.getBurnTime(itemStack, recipeType, fuelValues);
    }

    public T setBurnTime(int burnTime) {
        this.burnTime = burnTime;
        return (T) this;
    }
}
