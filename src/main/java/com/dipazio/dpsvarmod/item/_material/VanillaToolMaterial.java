package com.dipazio.dpsvarmod.item._material;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;

public class VanillaToolMaterial {
    public static final DpToolMaterial WOOD;
    public static final DpToolMaterial STONE;
    public static final DpToolMaterial IRON;
    public static final DpToolMaterial DIAMOND;
    public static final DpToolMaterial GOLD;
    public static final DpToolMaterial NETHERITE;

    static {
        WOOD = DpToolMaterial.createBasic(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 59, 2.0F, 15, ItemTags.WOODEN_TOOL_MATERIALS);
        STONE = DpToolMaterial.createBasic(BlockTags.INCORRECT_FOR_STONE_TOOL, 131, 4.0F, 5, ItemTags.STONE_TOOL_MATERIALS);
        IRON = DpToolMaterial.createBasic(BlockTags.INCORRECT_FOR_IRON_TOOL, 250, 6.0F, 14, ItemTags.IRON_TOOL_MATERIALS);
        DIAMOND = DpToolMaterial.createBasic(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 1561, 8.0F, 10, ItemTags.DIAMOND_TOOL_MATERIALS);
        GOLD = DpToolMaterial.createBasic(BlockTags.INCORRECT_FOR_GOLD_TOOL, 32, 12.0F, 22, ItemTags.GOLD_TOOL_MATERIALS);
        NETHERITE = DpToolMaterial.createBasic(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 2031, 9.0F, 15, ItemTags.NETHERITE_TOOL_MATERIALS);
    }
}
