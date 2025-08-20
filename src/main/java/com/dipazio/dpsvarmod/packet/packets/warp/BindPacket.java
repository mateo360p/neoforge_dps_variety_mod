package com.dipazio.dpsvarmod.packet.packets.warp;

import com.dipazio.dpsvarmod.item.warp.UnboundWarpPage;
import com.dipazio.dpsvarmod.packet.*;
import com.dipazio.dpsvarmod.register.DPsItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record BindPacket(InteractionHand hand, String name, boolean isPlayerBound) implements IPacketToServer {
    public static final StreamCodec<RegistryFriendlyByteBuf, BindPacket> STREAM_CODEC;

    static {
        STREAM_CODEC = StreamCodec.composite(
                StreamCodecs.HAND_CODEC, BindPacket::hand,
                StreamCodecs.STRING_CODEC, BindPacket::name,
                StreamCodecs.BOOLEAN_CODEC, BindPacket::isPlayerBound,
                BindPacket::new
        );
    }

    @Override
    public void onCall(ServerPlayer player) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && stack.getItem() == DPsItems.UNBOUND_WARP_PAGE.get()) {
            ItemStack single = new ItemStack((isPlayerBound) ? DPsItems.PLAYER_WARP_PAGE.get() : DPsItems.BOUND_WARP_PAGE.get());
            if (!isPlayerBound) {
                UnboundWarpPage.saveTeleport(single, player, name, player.level().dimension());
            } else {
                UnboundWarpPage.savePlayerTeleport(single, player);
            }

            stack.shrink(1);
            player.addItem(single);
        }
    }

    @Override
    public String getPacketPath() {
        return PacketHandler.BIND_WARP_PACKET_PATH;
    }
}