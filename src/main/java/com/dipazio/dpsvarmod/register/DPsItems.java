package com.dipazio.dpsvarmod.register;

import com.dipazio.dpsvarmod.DPsVarietyMod;
import com.dipazio.dpsvarmod.item.material.TripleAreaToolMaterial;
import com.dipazio.dpsvarmod.item.tools.Digger;
import com.dipazio.dpsvarmod.item.tools.Hammer;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DPsItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DPsVarietyMod.MODID);

    public static final DeferredItem<Item> WOODEN_DIGGER = ITEMS.registerItem("wooden_digger",
            (properties) -> new Digger(TripleAreaToolMaterial.WOOD, properties).setBurnTime(200));
    public static final DeferredItem<Item> STONE_DIGGER = ITEMS.registerItem("stone_digger",
            (properties) -> new Digger(TripleAreaToolMaterial.STONE, properties));
    public static final DeferredItem<Item> IRON_DIGGER = ITEMS.registerItem("iron_digger",
            (properties) -> new Digger(TripleAreaToolMaterial.IRON, properties));
    public static final DeferredItem<Item> GOLDEN_DIGGER = ITEMS.registerItem("golden_digger",
            (properties) -> new Digger(TripleAreaToolMaterial.GOLD, properties));
    public static final DeferredItem<Item> DIAMOND_DIGGER = ITEMS.registerItem("diamond_digger",
            (properties) -> new Digger(TripleAreaToolMaterial.DIAMOND, properties));

    public static final DeferredItem<Item> WOODEN_HAMMER = ITEMS.registerItem("wooden_hammer",
            (properties) -> new Hammer(TripleAreaToolMaterial.WOOD, properties).setBurnTime(200));
    public static final DeferredItem<Item> STONE_HAMMER = ITEMS.registerItem("stone_hammer",
            (properties) -> new Hammer(TripleAreaToolMaterial.STONE, properties));
    public static final DeferredItem<Item> IRON_HAMMER = ITEMS.registerItem("iron_hammer",
            (properties) -> new Hammer(TripleAreaToolMaterial.IRON, properties));
    public static final DeferredItem<Item> GOLDEN_HAMMER = ITEMS.registerItem("golden_hammer",
            (properties) -> new Hammer(TripleAreaToolMaterial.GOLD, properties));
    public static final DeferredItem<Item> DIAMOND_HAMMER = ITEMS.registerItem("diamond_hammer",
            (properties) -> new Hammer(TripleAreaToolMaterial.DIAMOND, properties));

    public static void registerItems(IEventBus eBus) {
        ITEMS.register(eBus);
    }
}
