package com.dipazio.dpsvarmod.item.tools;

import com.dipazio.dpsvarmod.item._parents.TripleAreaDestroyerTool;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ToolMaterial;

public class Hammer extends TripleAreaDestroyerTool {
    public Hammer(ToolMaterial tier, Properties properties) {
        super(tier, BlockTags.MINEABLE_WITH_PICKAXE, 5.5F, -3.5F, properties);
    }
}
