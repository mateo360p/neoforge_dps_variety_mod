package com.dipazio.dpsvarmod.packet;

import com.dipazio.dpsvarmod.DPsVarietyMod;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public interface IPacketExtension<P extends Player> extends CustomPacketPayload {
    default void doAction(IPayloadContext context) {
        Player base = context.player();
        P player = (P) base;
        context.enqueueWork(() -> onCall(player));
    }

    default @NotNull Type<? extends IPacketExtension<P>> type() {
        return new Type<>(ResourceLocation.fromNamespaceAndPath(DPsVarietyMod.MODID, getPacketPath()));
    }

    void onCall(P player);
    String getPacketPath();
}
