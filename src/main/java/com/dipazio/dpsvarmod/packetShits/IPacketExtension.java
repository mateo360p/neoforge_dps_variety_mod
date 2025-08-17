package com.dipazio.dpsvarmod.packetShits;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface IPacketExtension extends CustomPacketPayload {
    void doAction(IPayloadContext context);
}
