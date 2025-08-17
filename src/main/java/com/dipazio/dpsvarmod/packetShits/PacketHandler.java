package com.dipazio.dpsvarmod.packetShits;

import com.dipazio.dpsvarmod.DPsVarietyMod;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class PacketHandler {
    public static <T extends IPacketExtension> void registerMessage(PayloadRegistrar registrar, StreamCodec<? super RegistryFriendlyByteBuf, T> reader, IPacketExtension.Type<T> type, PacketFlow packetFlow) {
        registerMessage(registrar, reader, type, Optional.of(packetFlow));
    }

    public static <T extends IPacketExtension> void registerMessage(PayloadRegistrar pRegistrar, StreamCodec<? super RegistryFriendlyByteBuf, T> reader, IPacketExtension.Type<T> type, Optional<PacketFlow> packetFlow) {
        if (packetFlow.isPresent()) {
            if (packetFlow.get() == PacketFlow.CLIENTBOUND) pRegistrar.playToClient(type, reader, IPacketExtension::doAction);
            else pRegistrar.playToServer(type, reader, IPacketExtension::doAction);
        } else {
            pRegistrar.playBidirectional(type, reader, IPacketExtension::doAction);
        }

    }

    public static void sendToServer(IPacketExtension packet) {
        PacketDistributor.sendToServer(packet, new IPacketExtension[0]);
    }

    public static void sendToPlayer(ServerPlayer player, IPacketExtension packet) {
        PacketDistributor.sendToPlayer(player, packet, new IPacketExtension[0]);
    }

    public static void sendToAllClients(IPacketExtension packet) {
        PacketDistributor.sendToAllPlayers(packet, new IPacketExtension[0]);
    }

    public static void sendToPlayerInRadius(ServerLevel level, ServerPlayer player, Vec3 pos, double radius, IPacketExtension packet) {
        PacketDistributor.sendToPlayersNear(level, player, pos.x(), pos.y(), pos.z(), radius, packet, new IPacketExtension[0]);
    }

    public static void registerMessages(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(DPsVarietyMod.MODID);

        registerMessage(registrar, TeleportPacket.STREAM_CODEC, TeleportPacket.TYPE, PacketFlow.SERVERBOUND);
        registerMessage(registrar, BindPacket.STREAM_CODEC, BindPacket.TYPE, PacketFlow.SERVERBOUND);
    }
}
