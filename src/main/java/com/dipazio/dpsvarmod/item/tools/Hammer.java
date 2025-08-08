package com.dipazio.dpsvarmod.item.tools;

import com.dipazio.dpsvarmod.item._material.DpToolMaterial;
import com.dipazio.dpsvarmod.item._parents.TripleAreaDestroyerTool;
import net.minecraft.tags.BlockTags;


public class Hammer extends TripleAreaDestroyerTool {
    public Hammer(DpToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
        super(material, BlockTags.MINEABLE_WITH_PICKAXE, attackDamage, attackSpeed, properties);
    }
}
