package com.dipazio.dpsvarmod;

import com.dipazio.dpsvarmod.gui.screen.WarpBookScreen;
import com.dipazio.dpsvarmod.register.DPsBlocks;
import com.dipazio.dpsvarmod.register.DPsItems;
import com.dipazio.dpsvarmod.register.DPsMenus;
import com.dipazio.dpsvarmod.util.CreativeModeTabs;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
// Long time not seeing you buddy
@Mod(DPsVarietyMod.MODID)
public class DPsVarietyMod
{
    final String CODE_VERSION = "0.2.3.3"; // Just to verify the code version, this doesn't do anything!
    // Yes, i'm following a tutorial ok?
    public static final String MODID = "dpsvarmod";
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public DPsVarietyMod(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup); // Register the commonSetup method for modloading

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        CreativeModeTabs.register(modEventBus);

        DPsItems.registerItems(modEventBus);
        DPsBlocks.registerBlocks(modEventBus);
        DPsMenus.registerMenu(modEventBus);

        modEventBus.addListener(this::addCreative); // Register the item to a creative tab

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC); // Register our mod's ModConfigSpec so that FML can create and load the config file for us
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(DPsMenus.WARP_BOOK_MENU.get(), WarpBookScreen::new);
        }
    }
}