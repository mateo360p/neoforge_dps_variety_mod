package com.dipazio.dpsvarmod.item.warp;

import com.dipazio.dpsvarmod.util.ItemsFuncs;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class BoundWarpPage extends Item {

    public BoundWarpPage(Properties properties) {
        super(properties.stacksTo(16));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player.isShiftKeyDown()) {
            ItemStack stack = player.getItemInHand(hand);
            CompoundTag data = ItemsFuncs.getData(stack);
            doTeleportAction(data, (ServerPlayer) player);
            stack.shrink(1);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        CompoundTag data = ItemsFuncs.getData(stack);
        
        if (!hasTpData(ItemsFuncs.getData(stack))) {
            Component text = Component.translatable("info.warp.binds_none").withStyle(ChatFormatting.GRAY);
            tooltipComponents.add(text);
        } else {
            tooltipComponents.add(Component.literal(data.getString("waypoint_name")).withStyle(ChatFormatting.GRAY));
            Component text = Component.translatable("info.warp.binds_pos").append((
                    String.format("%.2f", data.getDouble("tp_X")) + ", " +
                    String.format("%.2f", data.getDouble("tp_Y")) + ", " +
                    String.format("%.2f", data.getDouble("tp_Z")))
                    ).withStyle(ChatFormatting.GRAY);
            tooltipComponents.add(text);
        }
    }

    public void doTeleportAction(CompoundTag data, ServerPlayer serverPlayer) {
        if (!hasTpData(data)) return;

        double x = data.getDouble("tp_X");
        double y = data.getDouble("tp_Y");
        double z = data.getDouble("tp_Z");
        double xRot = data.getDouble("rot_X");
        double yRot = data.getDouble("rot_Y");
        String dim = data.getString("dimension");

        teleportPlayer(serverPlayer, dim, x, y, z, xRot, yRot);
    }

    public static void teleportPlayer(
            ServerPlayer player, String dim,
            double x, double y, double z,
            double xRot, double yRot) {
        ResourceKey<Level> dimension = UnboundWarpPage.returnDimension(dim);

        if (!WarpBook.doFancyTeleport(
                player, dimension,
                x, y, z,
                xRot, yRot
        )) {
            WarpBook.sendCloudParticles(player);
        }
    }

    boolean hasTpData(CompoundTag data) {
        return data.contains("tp_X") && data.contains("tp_Y") && data.contains("tp_Z") && data.contains("waypoint_name");
    }
}