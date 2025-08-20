package com.dipazio.dpsvarmod.packet;

import com.dipazio.dpsvarmod.DPsVarietyMod;
import com.dipazio.dpsvarmod.packet.packets.warp.BindPacket;
import com.dipazio.dpsvarmod.packet.packets.warp.TeleportPacket;
import com.dipazio.dpsvarmod.packet.packets.warp.TeleportToPlayerPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
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
    public static final String TELEPORT_WARP_PACKET_PATH = "teleport_warp_packet";
    public static final String BIND_WARP_PACKET_PATH = "bind_warp_packet";
    public static final String TELEPORT_PLAYER_WARP_PACKET_PATH = "teleport_player_warp_packet";

    public static void registerMessages(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(DPsVarietyMod.MODID);

        registerMessage(registrar, TeleportPacket.STREAM_CODEC, TELEPORT_WARP_PACKET_PATH, PacketFlow.SERVERBOUND);
        registerMessage(registrar, BindPacket.STREAM_CODEC, BIND_WARP_PACKET_PATH, PacketFlow.SERVERBOUND);
        registerMessage(registrar, TeleportToPlayerPacket.STREAM_CODEC, TELEPORT_PLAYER_WARP_PACKET_PATH, PacketFlow.SERVERBOUND);
    }

    public static <T extends IPacketExtension> void registerMessage(
            PayloadRegistrar pRegistrar,
            StreamCodec<? super RegistryFriendlyByteBuf, T> codec,
            String path,
            PacketFlow packetFlow)
    {
        try {
            Optional<PacketFlow> optionalFlow = Optional.of(packetFlow);
            IPacketExtension.Type<T> type = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(DPsVarietyMod.MODID, path));

            if (optionalFlow.isPresent()) {
                if (optionalFlow.get() == PacketFlow.CLIENTBOUND) pRegistrar.playToClient(type, codec, IPacketExtension::doAction);
                else pRegistrar.playToServer(type, codec, IPacketExtension::doAction);
            } else {
                pRegistrar.playBidirectional(type, codec, IPacketExtension::doAction);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while registering packet with path: " + path, e);
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
}
