package com.dipazio.dpsvarmod.packet.packets.warp;

import com.dipazio.dpsvarmod.item.warp.UnboundWarpPage;
import com.dipazio.dpsvarmod.packet.IPacketExtension;
import com.dipazio.dpsvarmod.packet.PacketHandler;
import com.dipazio.dpsvarmod.packet.StreamCodecs;
import com.dipazio.dpsvarmod.register.DPsItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public record BindPacket(InteractionHand hand, String name) implements IPacketExtension {
    public static final StreamCodec<RegistryFriendlyByteBuf, BindPacket> STREAM_CODEC;

    static {
        STREAM_CODEC = StreamCodec.composite(
                StreamCodecs.HAND_CODEC, BindPacket::hand,
                StreamCodecs.STRING_CODEC, BindPacket::name,
                BindPacket::new
        );
    }

    @Override
    public void onCall(ServerPlayer player) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && stack.getItem() == DPsItems.UNBOUND_WARP_PAGE.get()) {
            ItemStack single = new ItemStack(DPsItems.BOUND_WARP_PAGE.get());
            UnboundWarpPage.saveTeleport(single, player, name);

            stack.shrink(1);
            player.addItem(single);
        }
    }

    @Override
    public String getPacketPath() {
        return PacketHandler.BIND_WARP_PACKET_PATH;
    }
}