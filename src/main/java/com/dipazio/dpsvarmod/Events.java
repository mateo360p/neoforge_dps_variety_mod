package com.dipazio.dpsvarmod;

import com.dipazio.dpsvarmod.packetShits.PacketHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = DPsVarietyMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Events {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PacketHandler.registerMessages(event);
    }
}