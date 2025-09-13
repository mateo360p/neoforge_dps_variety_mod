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
                        output.accept(DPsItems.COMBINED_ROTTEN_FLESH);
                        output.accept(DPsBlocks.APPLE_BASKET);
                        output.accept(DPsBlocks.CARROT_BASKET);
                        output.accept(DPsBlocks.POTATO_BASKET);
                        output.accept(DPsBlocks.BEETROOT_BASKET);
                        output.accept(DPsBlocks.NETHER_IRON_ORE);
                        output.accept(DPsBlocks.NETHER_DIAMOND_ORE);
                    }).build()
    );

    public static final Supplier<CreativeModeTab> DPS_TAB_VANILLA_MISSING = CREATIVE_MODE_TABS.register("dps_tab_vanilla_missing",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(DPsBlocks.STONE_TILES.get()))
                    .title(Component.translatable("creativetab.dpsvarmod.modtab_vanilla_missing"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(DPsBlocks.CHISELED_BLACKSTONE);

                        output.accept(DPsBlocks.QUARTZ_BLOCK_WALL);
                        output.accept(DPsBlocks.SMOOTH_QUARTZ_WALL);

                        output.accept(DPsBlocks.QUARTZ_BRICK_STAIRS);
                        output.accept(DPsBlocks.QUARTZ_BRICK_SLAB);
                        output.accept(DPsBlocks.QUARTZ_BRICK_WALL);

                        output.accept(DPsBlocks.POLISHED_GRANITE_WALL);
                        output.accept(DPsBlocks.POLISHED_GRANITE_BRICKS);
                        output.accept(DPsBlocks.POLISHED_GRANITE_BRICK_STAIRS);
                        output.accept(DPsBlocks.POLISHED_GRANITE_BRICK_SLAB);
                        output.accept(DPsBlocks.POLISHED_GRANITE_BRICK_WALL);

                        output.accept(DPsBlocks.POLISHED_DIORITE_WALL);
                        output.accept(DPsBlocks.POLISHED_DIORITE_BRICKS);
                        output.accept(DPsBlocks.POLISHED_DIORITE_BRICK_STAIRS);
                        output.accept(DPsBlocks.POLISHED_DIORITE_BRICK_SLAB);
                        output.accept(DPsBlocks.POLISHED_DIORITE_BRICK_WALL);

                        output.accept(DPsBlocks.POLISHED_ANDESITE_WALL);
                        output.accept(DPsBlocks.POLISHED_ANDESITE_BRICKS);
                        output.accept(DPsBlocks.POLISHED_ANDESITE_BRICK_STAIRS);
                        output.accept(DPsBlocks.POLISHED_ANDESITE_BRICK_SLAB);
                        output.accept(DPsBlocks.POLISHED_ANDESITE_BRICK_WALL);

                        output.accept(DPsBlocks.SMOOTH_STONE_STAIRS);
                        output.accept(DPsBlocks.SMOOTH_STONE_WALL);

                        output.accept(DPsBlocks.STONE_WALL);

                        output.accept(DPsBlocks.STONE_TILES);
                        output.accept(DPsBlocks.STONE_TILE_STAIRS);
                        output.accept(DPsBlocks.STONE_TILE_SLAB);
                        output.accept(DPsBlocks.STONE_TILE_WALL);

                        output.accept(DPsBlocks.DEEPSLATE_SLAB);
                        output.accept(DPsBlocks.DEEPSLATE_STAIRS);
                        output.accept(DPsBlocks.DEEPSLATE_WALL);

                        output.accept(DPsBlocks.DRIPSTONE_BLOCK_STAIRS);
                        output.accept(DPsBlocks.DRIPSTONE_BLOCK_SLAB);
                        output.accept(DPsBlocks.DRIPSTONE_BLOCK_WALL);

                        output.accept(DPsBlocks.CALCITE_STAIRS);
                        output.accept(DPsBlocks.CALCITE_SLAB);
                        output.accept(DPsBlocks.CALCITE_WALL);

                        output.accept(DPsBlocks.BASALT_STAIRS);
                        output.accept(DPsBlocks.BASALT_SLAB);
                        output.accept(DPsBlocks.BASALT_WALL);

                        output.accept(DPsBlocks.SMOOTH_BASALT_STAIRS);
                        output.accept(DPsBlocks.SMOOTH_BASALT_SLAB);
                        output.accept(DPsBlocks.SMOOTH_BASALT_WALL);

                        output.accept(DPsBlocks.POLISHED_BASALT_STAIRS);
                        output.accept(DPsBlocks.POLISHED_BASALT_SLAB);
                        output.accept(DPsBlocks.POLISHED_BASALT_WALL);

                        output.accept(DPsBlocks.POLISHED_BASALT_BRICKS);
                        output.accept(DPsBlocks.POLISHED_BASALT_BRICK_STAIRS);
                        output.accept(DPsBlocks.POLISHED_BASALT_BRICK_SLAB);
                        output.accept(DPsBlocks.POLISHED_BASALT_BRICK_WALL);

                        output.accept(DPsBlocks.SMOOTH_SANDSTONE_WALL);
                        output.accept(DPsBlocks.SANDSTONE_BRICKS);
                        output.accept(DPsBlocks.SANDSTONE_BRICK_STAIRS);
                        output.accept(DPsBlocks.SANDSTONE_BRICK_SLAB);
                        output.accept(DPsBlocks.SANDSTONE_BRICK_WALL);

                        output.accept(DPsBlocks.RED_SMOOTH_SANDSTONE_WALL);
                        output.accept(DPsBlocks.RED_SANDSTONE_BRICKS);
                        output.accept(DPsBlocks.RED_SANDSTONE_BRICK_STAIRS);
                        output.accept(DPsBlocks.RED_SANDSTONE_BRICK_SLAB);
                        output.accept(DPsBlocks.RED_SANDSTONE_BRICK_WALL);
                    }).build()
    );

    public static final Supplier<CreativeModeTab> DPS_TAB_WARP_BOOK = CREATIVE_MODE_TABS.register("dps_tab_warp_book",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(DPsItems.WARP_BOOK.get()))
                    .title(Component.translatable("creativetab.dpsvarmod.modtab_warp_book"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(DPsItems.WARP_BOOK);
                        output.accept(DPsItems.UNBOUND_WARP_PAGE);
                        output.accept(DPsItems.DEATH_WARP_PAGE);
                    }).build()
    );




    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
