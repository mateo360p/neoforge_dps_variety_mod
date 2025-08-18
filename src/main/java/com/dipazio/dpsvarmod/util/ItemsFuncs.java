package com.dipazio.dpsvarmod.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class ItemsFuncs {
    public static CompoundTag getData(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    public static void saveData(ItemStack stack, CompoundTag data) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
    }
}
