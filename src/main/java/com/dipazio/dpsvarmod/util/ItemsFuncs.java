package com.dipazio.dpsvarmod.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.List;

public class ItemsFuncs {
    public static CompoundTag getData(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    public static void saveData(ItemStack stack, CompoundTag data) {
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
    }

    public static boolean hasData(ItemStack stack, String name) {
        return getData(stack).contains(name);
    }

    public static boolean hasData(ItemStack stack, List<String> names) {
        for (String i : names ) {
            if (!hasData(stack, i)) return false;
        }
        return true;
    }
}
