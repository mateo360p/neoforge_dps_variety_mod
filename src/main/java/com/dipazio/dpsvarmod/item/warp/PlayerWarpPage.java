package com.dipazio.dpsvarmod.item.warp;

import com.dipazio.dpsvarmod.util.ItemsFuncs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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
import java.util.UUID;

public class PlayerWarpPage extends Item {

    public PlayerWarpPage(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player.isShiftKeyDown()) {
            ItemStack stack = player.getItemInHand(hand);
            CompoundTag data = ItemsFuncs.getData(stack);
            if ((player.getStringUUID().equals(data.getString("uuid")))) return InteractionResult.PASS;

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
            tooltipComponents.add(Component.literal(data.getString("player_name")).withStyle(ChatFormatting.GRAY));
            Player daPlayer = Minecraft.getInstance().player;
            if (daPlayer != null) {
                try {
                    UUID boundUUID = UUID.fromString(data.getString("uuid"));
                    if (boundUUID.equals(daPlayer.getUUID())) {
                        tooltipComponents.add(Component.literal("(That's you!, you cannot teleport to yourself)").withStyle(ChatFormatting.RED, ChatFormatting.ITALIC));
                    }
                } catch (Exception ignored) {}
            }
        }
    }

    public void doTeleportAction(CompoundTag data, ServerPlayer serverPlayer) {
        if (!hasTpData(data)) return;

        String playerUUID = data.getString("uuid");
        teleportPlayer(serverPlayer, playerUUID);
    }

    public static void teleportPlayer(ServerPlayer serverPlayer, String playerUUID) {
        ServerPlayer toPlayer = serverPlayer.getServer().getPlayerList().getPlayer(UUID.fromString(playerUUID));

        if (toPlayer == null) return;

        BoundWarpPage.teleportPlayer(serverPlayer, toPlayer.level().dimension(), toPlayer.getX(), toPlayer.getY(), toPlayer.getZ(), serverPlayer.getXRot(), serverPlayer.getYRot());
    }

    boolean hasTpData(CompoundTag data) {
        return data.contains("player_name") && data.contains("uuid");
    }
}