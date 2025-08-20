package com.dipazio.dpsvarmod.util;

import com.dipazio.dpsvarmod.DPsVarietyMod;
import com.dipazio.dpsvarmod.register.DPsBlocks;
import com.dipazio.dpsvarmod.register.DPsItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class CreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DPsVarietyMod.MODID);

    public static final Supplier<CreativeModeTab> DPS_TAB_MEGA_TOOLS = CREATIVE_MODE_TABS.register("dps_tab_mega_tools",
        () -> CreativeModeTab.builder().icon(() -> new ItemStack(DPsItems.GOLDEN_HAMMER.get()))
                .title(Component.translatable("creativetab.dpsvarmod.modtab_megatools"))
                .displayItems((itemDisplayParameters, output) -> {
                    output.accept(DPsItems.WOODEN_DIGGER);
                    output.accept(DPsItems.WOODEN_HAMMER);
                    output.accept(DPsItems.WOODEN_CHOPPER);
                    output.accept(DPsItems.WOODEN_TILLER);

                    output.accept(DPsItems.STONE_DIGGER);
                    output.accept(DPsItems.STONE_HAMMER);
                    output.accept(DPsItems.STONE_CHOPPER);
                    output.accept(DPsItems.STONE_TILLER);

                    output.accept(DPsItems.IRON_DIGGER);
                    output.accept(DPsItems.IRON_HAMMER);
                    output.accept(DPsItems.IRON_CHOPPER);
                    output.accept(DPsItems.IRON_TILLER);

                    output.accept(DPsItems.GOLDEN_DIGGER);
                    output.accept(DPsItems.GOLDEN_HAMMER);
                    output.accept(DPsItems.GOLDEN_CHOPPER);
                    output.accept(DPsItems.GOLDEN_TILLER);

                    output.accept(DPsItems.DIAMOND_DIGGER);
                    output.accept(DPsItems.DIAMOND_HAMMER);
                    output.accept(DPsItems.DIAMOND_CHOPPER);
                    output.accept(DPsItems.DIAMOND_TILLER);

                }).build()
    );

    public static final Supplier<CreativeModeTab> DPS_TAB_VANILLA_EXTRAS = CREATIVE_MODE_TABS.register("dps_tab_vanilla_extras",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(DPsBlocks.APPLE_BASKET.get()))
                    .title(Component.translatable("creativetab.dpsvarmod.modtab_vanilla_extras"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(DPsBlocks.APPLE_BASKET);
                        output.accept(DPsBlocks.CARROT_BASKET);
                        output.accept(DPsBlocks.POTATO_BASKET);
                        output.accept(DPsBlocks.BEETROOT_BASKET);
                        output.accept(DPsItems.COMBINED_ROTTEN_FLESH);

                        output.accept(DPsItems.WARP_BOOK);
                        output.accept(DPsItems.UNBOUND_WARP_PAGE);
                        output.accept(DPsItems.DEATH_WARP_PAGE);

                    }).build()
    );


    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
