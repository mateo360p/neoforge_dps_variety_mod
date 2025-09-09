package com.dipazio.dpsvarmod.register;

import com.dipazio.dpsvarmod.DPsVarietyMod;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class DPsBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(DPsVarietyMod.MODID);

    public static final DeferredBlock<Block> CARROT_BASKET = registerBlockItem("carrot_basket",
            (properties) -> new RotatedPillarBlock(properties.mapColor(MapColor.TERRACOTTA_ORANGE).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()));
    public static final DeferredBlock<Block> POTATO_BASKET = registerBlockItem("potato_basket",
            (properties) -> new RotatedPillarBlock(properties.mapColor(MapColor.TERRACOTTA_YELLOW).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()));
    public static final DeferredBlock<Block> APPLE_BASKET = registerBlockItem("apple_basket",
            (properties) -> new RotatedPillarBlock(properties.mapColor(MapColor.TERRACOTTA_RED).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()));
    public static final DeferredBlock<Block> BEETROOT_BASKET = registerBlockItem("beetroot_basket",
            (properties) -> new RotatedPillarBlock(properties.mapColor(MapColor.TERRACOTTA_PINK).instrument(NoteBlockInstrument.BASS).strength(2.0F).sound(SoundType.WOOD).ignitedByLava()));

    public static final DeferredBlock<Block> NETHER_IRON_ORE = registerBlockItem("nether_iron_ore",
            (properties) -> new DropExperienceBlock(UniformInt.of(0, 1), properties.mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.NETHER_GOLD_ORE)));

    public static final DeferredBlock<Block> NETHER_DIAMOND_ORE = registerBlockItem("nether_diamond_ore",
            (properties) -> new DropExperienceBlock(UniformInt.of(4, 8), properties.mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.NETHER_GOLD_ORE)));

    // fkin hell bro
    public static final DeferredBlock<Block> STONE_WALL = registerBlockItem("stone_wall",
            (properties) -> new WallBlock(properties.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));

    public static final DeferredBlock<Block> CHISELED_BLACKSTONE = registerBlockItem("chiseled_blackstone",
            (properties) -> new Block(properties.mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));

    public static final DeferredBlock<Block> STONE_TILES = registerBlockItem("stone_tiles",
            (properties) -> new Block(properties.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<StairBlock> STONE_TILE_STAIRS = registerBlockItem("stone_tile_stairs",
            (properties) -> new StairBlock(DPsBlocks.STONE_TILES.get().defaultBlockState(),
                    properties.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> STONE_TILE_SLAB = registerBlockItem("stone_tile_slab",
            (properties) -> new SlabBlock(properties.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> STONE_TILE_WALL = registerBlockItem("stone_tile_wall",
            (properties) -> new WallBlock(properties.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));

    public static final DeferredBlock<StairBlock> DEEPSLATE_STAIRS = registerBlockItem("deepslate_stairs",
            (properties) -> new StairBlock(Blocks.DEEPSLATE.defaultBlockState(),
                    properties.mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.DEEPSLATE)));
    public static final DeferredBlock<Block> DEEPSLATE_SLAB = registerBlockItem("deepslate_slab",
            (properties) -> new SlabBlock(properties.mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.DEEPSLATE)));
    public static final DeferredBlock<Block> DEEPSLATE_WALL = registerBlockItem("deepslate_wall",
            (properties) -> new WallBlock(properties.mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.DEEPSLATE)));

    public static final DeferredBlock<Block> DRIPSTONE_BLOCK_WALL = registerBlockItem("dripstone_block_wall",
            (properties) -> new WallBlock(properties.mapColor(MapColor.TERRACOTTA_BROWN).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DRIPSTONE_BLOCK).requiresCorrectToolForDrops().strength(1.5F, 1.0F)));
    public static final DeferredBlock<Block> DRIPSTONE_BLOCK_STAIRS = registerBlockItem("dripstone_block_stairs",
            (properties) -> new StairBlock(Blocks.DRIPSTONE_BLOCK.defaultBlockState(),
                    properties.mapColor(MapColor.TERRACOTTA_BROWN).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DRIPSTONE_BLOCK).requiresCorrectToolForDrops().strength(1.5F, 1.0F)));
    public static final DeferredBlock<Block> DRIPSTONE_BLOCK_SLAB = registerBlockItem("dripstone_block_slab",
            (properties) -> new SlabBlock(properties.mapColor(MapColor.TERRACOTTA_BROWN).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.DRIPSTONE_BLOCK).requiresCorrectToolForDrops().strength(1.5F, 1.0F)));

    public static final DeferredBlock<Block> CALCITE_WALL = registerBlockItem("calcite_wall",
            (properties) -> new WallBlock(properties.mapColor(MapColor.TERRACOTTA_WHITE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.CALCITE).requiresCorrectToolForDrops().strength(0.75F)));
    public static final DeferredBlock<Block> CALCITE_STAIRS = registerBlockItem("calcite_stairs",
            (properties) -> new StairBlock(Blocks.CALCITE.defaultBlockState(),
                    properties.mapColor(MapColor.TERRACOTTA_WHITE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.CALCITE).requiresCorrectToolForDrops().strength(0.75F)));
    public static final DeferredBlock<Block> CALCITE_SLAB = registerBlockItem("calcite_slab",
            (properties) -> new SlabBlock(properties.mapColor(MapColor.TERRACOTTA_WHITE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.CALCITE).requiresCorrectToolForDrops().strength(0.75F)));

    public static final DeferredBlock<Block> BASALT_WALL = registerBlockItem("basalt_wall",
            (properties) -> new WallBlock(properties.mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.25F, 4.2F).sound(SoundType.BASALT)));
    public static final DeferredBlock<Block> BASALT_STAIRS = registerBlockItem("basalt_stairs",
            (properties) -> new StairBlock(Blocks.BASALT.defaultBlockState(),
                    properties.mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.25F, 4.2F).sound(SoundType.BASALT)));
    public static final DeferredBlock<Block> BASALT_SLAB = registerBlockItem("basalt_slab",
            (properties) -> new SlabBlock(properties.mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.25F, 4.2F).sound(SoundType.BASALT)));

    public static final DeferredBlock<Block> SMOOTH_BASALT_WALL = registerBlockItem("smooth_basalt_wall",
            (properties) -> new WallBlock(properties.mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.25F, 4.2F).sound(SoundType.BASALT)));
    public static final DeferredBlock<Block> SMOOTH_BASALT_STAIRS = registerBlockItem("smooth_basalt_stairs",
            (properties) -> new StairBlock(Blocks.SMOOTH_BASALT.defaultBlockState(),
                    properties.mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.25F, 4.2F).sound(SoundType.BASALT)));
    public static final DeferredBlock<Block> SMOOTH_BASALT_SLAB = registerBlockItem("smooth_basalt_slab",
            (properties) -> new SlabBlock(properties.mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.25F, 4.2F).sound(SoundType.BASALT)));

    public static final DeferredBlock<Block> POLISHED_BASALT_WALL = registerBlockItem("polished_basalt_wall",
            (properties) -> new WallBlock(properties.mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.25F, 4.2F).sound(SoundType.BASALT)));
    public static final DeferredBlock<Block> POLISHED_BASALT_STAIRS = registerBlockItem("polished_basalt_stairs",
            (properties) -> new StairBlock(Blocks.POLISHED_BASALT.defaultBlockState(),
                    properties));
    public static final DeferredBlock<Block> POLISHED_BASALT_SLAB = registerBlockItem("polished_basalt_slab",
            (properties) -> new SlabBlock(properties.mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.25F, 4.2F).sound(SoundType.BASALT)));

    public static final DeferredBlock<Block> SMOOTH_STONE_STAIRS = registerBlockItem("smooth_stone_stairs",
            (properties) -> new StairBlock(Blocks.SMOOTH_STONE.defaultBlockState(),
                    properties.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
    public static final DeferredBlock<Block> SMOOTH_STONE_WALL = registerBlockItem("smooth_stone_wall",
            (properties) -> new WallBlock(properties.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));

    public static final DeferredBlock<Block> POLISHED_GRANITE_WALL = registerBlockItem("polished_granite_wall",
            (properties) -> new WallBlock(properties.mapColor(MapColor.DIRT).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> POLISHED_DIORITE_WALL = registerBlockItem("polished_diorite_wall",
            (properties) -> new WallBlock(properties.mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));
    public static final DeferredBlock<Block> POLISHED_ANDESITE_WALL = registerBlockItem("polished_andesite_wall",
            (properties) -> new WallBlock(properties.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)));

    // Are this two different-?
    public static final DeferredBlock<Block> QUARTZ_BLOCK_WALL = registerBlockItem("quartz_block_wall",
            (properties) -> new WallBlock(properties.mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final DeferredBlock<Block> SMOOTH_QUARTZ_WALL = registerBlockItem("smooth_quartz_wall",
            (properties) -> new WallBlock(properties.mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));

    public static final DeferredBlock<Block> QUARTZ_BRICK_STAIRS = registerBlockItem("quartz_brick_stairs",
            (properties) -> new StairBlock(Blocks.QUARTZ_BRICKS.defaultBlockState(),
                    properties.mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final DeferredBlock<Block> QUARTZ_BRICK_SLAB = registerBlockItem("quartz_brick_slab",
            (properties) -> new SlabBlock(properties.mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final DeferredBlock<Block> QUARTZ_BRICK_WALL = registerBlockItem("quartz_brick_wall",
            (properties) -> new WallBlock(properties.mapColor(MapColor.QUARTZ).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));

    public static <B extends Block> DeferredBlock<B> registerBlockItem(String name, Function<BlockBehaviour.Properties, ? extends B> func) {
        DeferredBlock<B> daBlock = registerBlock(name, func);
        DPsItems.ITEMS.registerItem(name, (properties) -> new BlockItem(daBlock.get(), properties.useBlockDescriptionPrefix()));
        return daBlock;
    }

    public static <B extends Block> DeferredBlock<B> registerBlock(String name, Function<BlockBehaviour.Properties, ? extends B> func) {
        return BLOCKS.registerBlock(name, func);
    }

    public static void registerBlocks(IEventBus eBus) {
        BLOCKS.register(eBus);
    }
}
