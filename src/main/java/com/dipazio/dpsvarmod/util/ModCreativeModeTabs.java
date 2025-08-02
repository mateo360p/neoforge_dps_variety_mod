package com.dipazio.dpsvarmod.util;

import com.dipazio.dpsvarmod.DPsVarietyMod;
import com.dipazio.dpsvarmod.register.DPsItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DPsVarietyMod.MODID);

    public static final Supplier<CreativeModeTab> DPS_TAB = CREATIVE_MODE_TABS.register("dps_tab",
        () -> CreativeModeTab.builder().icon(() -> new ItemStack(DPsItems.IRON_HAMMER.get()))
                .title(Component.translatable("creativetab.dpsvarmod.modtab"))
                .displayItems((itemDisplayParameters, output) -> {
                    output.accept(DPsItems.WOODEN_HAMMER);
                    output.accept(DPsItems.STONE_HAMMER);
                    output.accept(DPsItems.IRON_HAMMER);
                    output.accept(DPsItems.GOLDEN_HAMMER);
                    output.accept(DPsItems.DIAMOND_HAMMER);
                }).build()
    );

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
