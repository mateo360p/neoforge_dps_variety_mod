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

    public static final DeferredBlock<Block> NETHER_IRON_ORE = registerBlockItem("nether_iron_ore",
            (properties) -> new DropExperienceBlock(UniformInt.of(0, 1), properties.mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.NETHER_GOLD_ORE)));

    public static final DeferredBlock<Block> NETHER_DIAMOND_ORE = registerBlockItem("nether_diamond_ore",
            (properties) -> new DropExperienceBlock(UniformInt.of(4, 8), properties.mapColor(MapColor.NETHER).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(3.0F, 3.0F).sound(SoundType.NETHER_GOLD_ORE)));

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
