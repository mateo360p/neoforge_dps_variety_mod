package com.dipazio.dpsvarmod.packet;

import com.dipazio.dpsvarmod.DPsVarietyMod;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public interface IPacketExtension<T extends IPacketExtension<T>> extends CustomPacketPayload {
    default void doAction(IPayloadContext context) {
        ServerPlayer player = (ServerPlayer) context.player();
        context.enqueueWork(() -> onCall(player));
    }

    default @NotNull Type<T> type() {
        return new Type<>(ResourceLocation.fromNamespaceAndPath(DPsVarietyMod.MODID, getPacketPath()));
    }

    void onCall(ServerPlayer player);
    String getPacketPath();
}
