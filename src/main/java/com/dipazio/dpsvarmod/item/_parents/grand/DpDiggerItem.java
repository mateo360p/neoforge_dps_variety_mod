package com.dipazio.dpsvarmod.item._parents.grand;

import com.dipazio.dpsvarmod.item._material.DpToolMaterial;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class DpDiggerItem extends DpItem {
    public DpDiggerItem(DpToolMaterial material, TagKey<Block> mineableBlocks, float attackDamage, float attackSpeed, Item.Properties properties) {
        super(material.applyToolProperties(properties, mineableBlocks, attackDamage, attackSpeed));
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack itemStack, LivingEntity entity1, LivingEntity entity2) {
        itemStack.hurtAndBreak(2, entity2, EquipmentSlot.MAINHAND);
    }
}
