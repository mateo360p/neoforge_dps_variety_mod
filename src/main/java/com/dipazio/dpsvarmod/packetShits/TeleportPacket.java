package com.dipazio.dpsvarmod.packetShits;

import com.dipazio.dpsvarmod.DPsVarietyMod;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

// I hate packets
public record TeleportPacket(double x, double y, double z) implements IPacketExtension {
    public static final CustomPacketPayload.Type<TeleportPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(DPsVarietyMod.MODID,"packet_teleport"));

    public static final StreamCodec<RegistryFriendlyByteBuf, TeleportPacket> STREAM_CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, Double> DOUBLE_CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf buf, Double value) {
            buf.writeDouble(value);
        }

        @Override
        public Double decode(RegistryFriendlyByteBuf buf) {
            return buf.readDouble();
        }
    };

    public void doAction(@NotNull IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        if (player == null) return; // idk
        context.enqueueWork(() -> player.teleportTo(x, y, z));
    }

    static {
        STREAM_CODEC = StreamCodec.composite(
                DOUBLE_CODEC, TeleportPacket::x,
                DOUBLE_CODEC, TeleportPacket::y,
                DOUBLE_CODEC, TeleportPacket::z,
                TeleportPacket::new
        );
    }

    public CustomPacketPayload.@NotNull Type<TeleportPacket> type() {
        return TYPE;
    }
}