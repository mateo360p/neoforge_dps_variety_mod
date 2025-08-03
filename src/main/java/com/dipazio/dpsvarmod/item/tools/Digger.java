package com.dipazio.dpsvarmod.item.tools;

import com.dipazio.dpsvarmod.item.parents.TripleAreaDestroyerTool;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.FuelValues;
import org.jetbrains.annotations.Nullable;

public class Digger extends TripleAreaDestroyerTool {
    public Digger(ToolMaterial tier, Properties properties) {
        super(tier, BlockTags.MINEABLE_WITH_SHOVEL, 6.0F, -3.7F, properties);
    }
}
