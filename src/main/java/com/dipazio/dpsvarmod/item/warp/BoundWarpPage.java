package com.dipazio.dpsvarmod.item.warp;

import com.dipazio.dpsvarmod.util.ItemsFuncs;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BoundWarpPage extends Item {

    public BoundWarpPage(Properties properties) {
        super(properties);
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
        if (!hasTpData(data)) return;

        double x = data.getDouble("tp_X");
        double y = data.getDouble("tp_Y");
        double z = data.getDouble("tp_Z");
        double xRot = data.getDouble("rot_X");
        double yRot = data.getDouble("rot_Y");
        String dim = data.getString("dimension");

        teleportPlayer(serverPlayer, UnboundWarpPage.returnDimension(dim), x, y, z, xRot, yRot);
    }

    public static void teleportPlayer(
            ServerPlayer player, ResourceKey<Level> dimension,
            double x, double y, double z,
            double xRotation, double yRotation) {
        if (dimension == null) return;
        ServerLevel newLevel = player.getServer().getLevel(dimension);

        if (newLevel == null) return;
        player.teleport(new TeleportTransition(newLevel, new Vec3(x, y, z), Vec3.ZERO, (float) yRotation, (float) xRotation, TeleportTransition.DO_NOTHING));
    }

    boolean hasTpData(CompoundTag data) {
        return data.contains("tp_X") && data.contains("tp_Y") && data.contains("tp_Z") && data.contains("waypoint_name");
    }

    public static double calcWarpHungerExhaustion(double distance, Difficulty difficulty) {
        distance = Mth.clamp(distance, 250.0, 20000.0);
        double distTax = distance * 0.005;
        double diffTax = switch (difficulty) {
            case EASY -> 1.0;
            case HARD -> 2.25;
            case PEACEFUL -> 0.0;
            default -> 1.75; // NORMAL and others idk
        };

        return distTax * diffTax;
    }
}