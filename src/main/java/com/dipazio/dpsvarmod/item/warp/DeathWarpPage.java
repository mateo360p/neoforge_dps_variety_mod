package com.dipazio.dpsvarmod.item.warp;

import com.dipazio.dpsvarmod.register.DPsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class DeathWarpPage extends Item {

    public DeathWarpPage(Properties properties) {
        super(properties.stacksTo(16).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        Component text = Component.translatable("info.warp.binds_death", Component.translatable(DPsItems.WARP_BOOK.get().getDescriptionId())).withStyle(ChatFormatting.GRAY);
        tooltipComponents.add(text);
    }
}