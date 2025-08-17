package com.dipazio.dpsvarmod.item.warp;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

// I was changing the models of the Unbound page
// Man that shit is stressing, let's do 2 pages
// Unbound and Bound are separated
public class BoundWarpPage extends Item {

    public BoundWarpPage(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && !player.isShiftKeyDown()) {
            ItemStack stack = player.getItemInHand(hand);
            CompoundTag data = UnboundWarpPage.getData(stack);
            doTeleportAction(data, (ServerPlayer) player);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        CompoundTag data = UnboundWarpPage.getData(stack);
        if (!UnboundWarpPage.hasTpData(UnboundWarpPage.getData(stack))) {
            Component text = Component.literal("No teleport data").withStyle(ChatFormatting.GRAY);
            tooltipComponents.add(text);
        } else {
            tooltipComponents.add(Component.literal(data.getString("waypoint_name")).withStyle(ChatFormatting.GRAY));
            Component text = Component.literal("Saved position: " +
                    String.format("%.2f", data.getDouble("tp_X")) + ", " +
                    String.format("%.2f", data.getDouble("tp_Y")) + ", " +
                    String.format("%.2f", data.getDouble("tp_Z")))
                    .withStyle(ChatFormatting.GRAY);
            tooltipComponents.add(text);
        }
    }

    public void doTeleportAction(CompoundTag data, ServerPlayer serverPlayer) {
        if (!UnboundWarpPage.hasTpData(data)) return;

        double x = data.getDouble("tp_X");
        double y = data.getDouble("tp_Y");
        double z = data.getDouble("tp_Z");

        serverPlayer.teleportTo(x, y, z);
    }
}