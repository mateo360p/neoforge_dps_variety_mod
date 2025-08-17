package com.dipazio.dpsvarmod.packetShits;

import com.dipazio.dpsvarmod.DPsVarietyMod;
import com.dipazio.dpsvarmod.item.warp.UnboundWarpPage;
import com.dipazio.dpsvarmod.register.DPsItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record BindPacket(InteractionHand hand, String name) implements IPacketExtension {
    public static final Type<BindPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(DPsVarietyMod.MODID,"packet_bind"));

    public static final net.minecraft.network.codec.StreamCodec<RegistryFriendlyByteBuf, BindPacket> STREAM_CODEC;
    public static final net.minecraft.network.codec.StreamCodec<RegistryFriendlyByteBuf, InteractionHand> HAND_CODEC = new net.minecraft.network.codec.StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf buf, InteractionHand hand) {
            buf.writeInt(hand.ordinal());
        }

        @Override
        public InteractionHand decode(RegistryFriendlyByteBuf buf) {
            return InteractionHand.values()[buf.readInt()];
        }
    };
    public static final net.minecraft.network.codec.StreamCodec<RegistryFriendlyByteBuf, String> STRING_CODEC = StreamCodec.STRING_UTF8;
    public static final net.minecraft.network.codec.StreamCodec<RegistryFriendlyByteBuf, ItemStack> ITEM_STACK_CODEC = ItemStack.OPTIONAL_STREAM_CODEC;

    public void doAction(IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        context.enqueueWork(() -> {
            ItemStack stack = player.getItemInHand(hand);
            if (!stack.isEmpty() && stack.getItem() == DPsItems.UNBOUND_WARP_PAGE.get()) {
                ItemStack single = new ItemStack(DPsItems.BOUND_WARP_PAGE.get());
                UnboundWarpPage.saveTeleport(single, player, name);

                stack.shrink(1);
                player.addItem(single);
            }
        });
    }

    static {
        STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(
                HAND_CODEC, BindPacket::hand,
                STRING_CODEC, BindPacket::name,
                BindPacket::new
        );
    }

    public CustomPacketPayload.@NotNull Type<BindPacket> type() {
        return TYPE;
    }
}