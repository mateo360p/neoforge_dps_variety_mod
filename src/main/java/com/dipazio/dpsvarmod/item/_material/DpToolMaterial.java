package com.dipazio.dpsvarmod.item._material;

import com.dipazio.dpsvarmod.item._parents.grand.DpItem;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

// I found the minecraft tools speed and damage system AWFUL
// So here's my system!, you have to put the exact value you want
// Your Axe with your 9.0F value will have 9.0F! (***should have)
public class DpToolMaterial {
    private int durability;
    private float toolSpeed;
    private float attackDamageBonus;
    private float attackSpeedBonus; // NEW!
    private TagKey<Block> incorrectBlocksForDrops;
    private int enchantmentValue;
    private TagKey<Item> repairItems;

    private DpToolMaterial(TagKey<Block> incorrectBlocksForDrops, int durability, float toolSpeed, float attackDamageBonus, float attackSpeedBonus, int enchantmentValue, TagKey<Item> repairItems) {
        this.incorrectBlocksForDrops = incorrectBlocksForDrops;
        this.durability = durability;
        this.toolSpeed = toolSpeed;
        this.attackDamageBonus = attackDamageBonus;
        this.attackSpeedBonus = attackSpeedBonus;
        this.enchantmentValue = enchantmentValue;
        this.repairItems = repairItems;
    }

    public static DpToolMaterial createBasic(TagKey<Block> incorrectBlocksForDrops, int durability, float toolSpeed, int enchantmentValue, TagKey<Item> repairItems) {
        return create(
                incorrectBlocksForDrops,
                durability,
                toolSpeed,
                0,
                0,
                enchantmentValue,
                repairItems
        );
    }

    public static DpToolMaterial create(TagKey<Block> incorrectBlocksForDrops, int durability, float toolSpeed, float attackDamageBonus, float attackSpeedBonus, int enchantmentValue, TagKey<Item> repairItems) {
        return new DpToolMaterial(
            incorrectBlocksForDrops,
            durability,
            toolSpeed,
            attackDamageBonus - 1.0F,
            attackSpeedBonus - 4.0F,
            enchantmentValue,
            repairItems
        );
    }

    public static DpToolMaterial copyFrom(DpToolMaterial original) {
        return new DpToolMaterial(
                original.incorrectBlocksForDrops,
                original.durability,
                original.toolSpeed,
                original.attackDamageBonus,
                original.attackSpeedBonus,
                original.enchantmentValue,
                original.repairItems
        );
    }

    public DpToolMaterial setDurability(int newDurability) {
        this.durability = newDurability;
        return this;
    }

    public DpToolMaterial setToolSpeed(int newToolSpeed) {
        this.toolSpeed = newToolSpeed;
        return this;
    }

    public DpToolMaterial setAttackDamageBonus(int newDamage) {
        this.attackDamageBonus = newDamage - 1.0F;
        return this;
    }

    public DpToolMaterial setAttackSpeedBonus(int newSpeed) {
        this.attackSpeedBonus = newSpeed - 4.0F;
        return this;
    }

    // Private Methods -----------------------------------------------
    private Item.Properties applyCommonProperties(Item.Properties properties) {
        return properties.durability(this.durability)
                .repairable(this.repairItems)
                .enchantable(this.enchantmentValue);
    }

    private ItemAttributeModifiers createToolAttributes(float attackDamage, float attackSpeed) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,
                                (double)(attackDamage + this.attackDamageBonus),
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(Item.BASE_ATTACK_SPEED_ID,
                                (double)(attackSpeed + this.attackSpeedBonus),
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    private ItemAttributeModifiers createSwordAttributes(float attackDamage, float attackSpeed) {
        return ItemAttributeModifiers.builder().
                add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,
                                (double)(attackDamage + this.attackDamageBonus),
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(Item.BASE_ATTACK_SPEED_ID,
                                (double)(attackSpeed + this.attackSpeedBonus),
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND)
                .build();
    }

    // Public functions -----------------------------------------------
    public Item.Properties applyToolProperties(Item.Properties properties, TagKey<Block> mineableBlocks, float attackDamage, float attackSpeed) {
        HolderGetter<Block> holdergetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
        return this.applyCommonProperties(properties)
                .component(DataComponents.TOOL, new Tool(
                        List.of(Tool.Rule.deniesDrops(holdergetter.getOrThrow(this.incorrectBlocksForDrops)),
                                Tool.Rule.minesAndDrops(holdergetter.getOrThrow(mineableBlocks), this.toolSpeed)),
                        1.0F,
                        1))
                .attributes(this.createToolAttributes(
                        attackDamage,
                        attackSpeed));
    }

    public Item.Properties applySwordProperties(Item.Properties properties, float attackDamage, float attackSpeed) {
        HolderGetter<Block> holdergetter = BuiltInRegistries.acquireBootstrapRegistrationLookup(BuiltInRegistries.BLOCK);
        return this.applyCommonProperties(properties)
                .component(DataComponents.TOOL, new Tool(
                        List.of(Tool.Rule.minesAndDrops(
                                        HolderSet.direct(new Holder[]{Blocks.COBWEB.builtInRegistryHolder()}),
                                        15.0F),
                                Tool.Rule.overrideSpeed(
                                        holdergetter.getOrThrow(BlockTags.SWORD_EFFICIENT),
                                        1.5F)),
                        1.0F,
                        2))
                .attributes(this.createSwordAttributes(
                        attackDamage,
                        attackSpeed));
    }
}
