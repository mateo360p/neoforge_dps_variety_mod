package com.dipazio.dpsvarmod.item.material;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ToolMaterial;

public class TripleAreaToolMaterial {
    public static final ToolMaterial WOOD;
    public static final ToolMaterial STONE;
    public static final ToolMaterial IRON;
    public static final ToolMaterial DIAMOND;
    public static final ToolMaterial GOLD;
    public static final ToolMaterial NETHERITE;

    // Looks crazy, but actually you have less durability (I think-)
    static {
        WOOD = new ToolMaterial(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 327, 2.0F, 0.0F, 15, ItemTags.WOODEN_TOOL_MATERIALS);
        STONE = new ToolMaterial(BlockTags.INCORRECT_FOR_STONE_TOOL, 727, 4.0F, 1.0F, 5, ItemTags.STONE_TOOL_MATERIALS);
        IRON = new ToolMaterial(BlockTags.INCORRECT_FOR_IRON_TOOL, 1387, 6.0F, 2.0F, 14, ItemTags.IRON_TOOL_MATERIALS);
        DIAMOND = new ToolMaterial(BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 8663, 8.0F, 3.0F, 10, ItemTags.DIAMOND_TOOL_MATERIALS);
        GOLD = new ToolMaterial(BlockTags.INCORRECT_FOR_GOLD_TOOL, 177, 12.0F, 0.0F, 22, ItemTags.GOLD_TOOL_MATERIALS);
        NETHERITE = new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 11272, 9.0F, 4.0F, 15, ItemTags.NETHERITE_TOOL_MATERIALS);
    }
    // I have a python file with the "formula" to get these durabilities in the _assets folder :v
    // However I should calculate better the durability, because now it's kinda OP
}
