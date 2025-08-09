package com.dipazio.dpsvarmod.register;

import com.dipazio.dpsvarmod.DPsVarietyMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
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
