package com.dipazio.dpsvarmod.item.tools;

import com.dipazio.dpsvarmod.item.parents.grand.DpDiggerItem;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

// No, this ain't a fkin' helicopter man!
public class Chopper extends DpDiggerItem {
    public Chopper(ToolMaterial material, TagKey<Block> mineableBlocks, float attackDamage, float attackSpeed, Properties properties) {
        super(material, mineableBlocks, attackDamage, attackSpeed, properties);
    }
}
