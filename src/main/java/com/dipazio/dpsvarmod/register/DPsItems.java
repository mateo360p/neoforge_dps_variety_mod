package com.dipazio.dpsvarmod.register;

import com.dipazio.dpsvarmod.DPsVarietyMod;
import com.dipazio.dpsvarmod.item._material.MegaToolMaterial;
import com.dipazio.dpsvarmod.item.tools.Chopper;
import com.dipazio.dpsvarmod.item.tools.Digger;
import com.dipazio.dpsvarmod.item.tools.Hammer;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DPsItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DPsVarietyMod.MODID);

    public static final DeferredItem<Item> WOODEN_DIGGER = ITEMS.registerItem("wooden_digger",
            (properties) -> new Digger(MegaToolMaterial.WOOD, properties).setBurnTime(200));
    public static final DeferredItem<Item> STONE_DIGGER = ITEMS.registerItem("stone_digger",
            (properties) -> new Digger(MegaToolMaterial.STONE, properties));
    public static final DeferredItem<Item> IRON_DIGGER = ITEMS.registerItem("iron_digger",
            (properties) -> new Digger(MegaToolMaterial.IRON, properties));
    public static final DeferredItem<Item> GOLDEN_DIGGER = ITEMS.registerItem("golden_digger",
            (properties) -> new Digger(MegaToolMaterial.GOLD, properties));
    public static final DeferredItem<Item> DIAMOND_DIGGER = ITEMS.registerItem("diamond_digger",
            (properties) -> new Digger(MegaToolMaterial.DIAMOND, properties));

    public static final DeferredItem<Item> WOODEN_HAMMER = ITEMS.registerItem("wooden_hammer",
            (properties) -> new Hammer(MegaToolMaterial.WOOD, properties).setBurnTime(200));
    public static final DeferredItem<Item> STONE_HAMMER = ITEMS.registerItem("stone_hammer",
            (properties) -> new Hammer(MegaToolMaterial.STONE, properties));
    public static final DeferredItem<Item> IRON_HAMMER = ITEMS.registerItem("iron_hammer",
            (properties) -> new Hammer(MegaToolMaterial.IRON, properties));
    public static final DeferredItem<Item> GOLDEN_HAMMER = ITEMS.registerItem("golden_hammer",
            (properties) -> new Hammer(MegaToolMaterial.GOLD, properties));
    public static final DeferredItem<Item> DIAMOND_HAMMER = ITEMS.registerItem("diamond_hammer",
            (properties) -> new Hammer(MegaToolMaterial.DIAMOND, properties));

    public static final DeferredItem<Item> WOODEN_CHOPPER = ITEMS.registerItem("wooden_chopper",
            (properties) -> new Chopper(MegaToolMaterial.WOOD, 10, properties).setBurnTime(200));
    public static final DeferredItem<Item> STONE_CHOPPER = ITEMS.registerItem("stone_chopper",
            (properties) -> new Chopper(MegaToolMaterial.STONE, 20, properties));
    public static final DeferredItem<Item> IRON_CHOPPER = ITEMS.registerItem("iron_chopper",
            (properties) -> new Chopper(MegaToolMaterial.IRON, 32, properties));
    public static final DeferredItem<Item> GOLDEN_CHOPPER = ITEMS.registerItem("golden_chopper",
            (properties) -> new Chopper(MegaToolMaterial.GOLD, 10, properties)); // bruh
    public static final DeferredItem<Item> DIAMOND_CHOPPER = ITEMS.registerItem("diamond_chopper",
            (properties) -> new Chopper(MegaToolMaterial.DIAMOND, 64, properties));

    public static void registerItems(IEventBus eBus) {
        ITEMS.register(eBus);
    }
}
