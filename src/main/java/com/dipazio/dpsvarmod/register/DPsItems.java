package com.dipazio.dpsvarmod.register;

import com.dipazio.dpsvarmod.DPsVarietyMod;
import com.dipazio.dpsvarmod.item.material.TripleAreaToolMaterial;
import com.dipazio.dpsvarmod.item.tools.Hammer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DPsItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DPsVarietyMod.MODID);

    // TEST public static final DeferredItem<Item> OIL_BOTTLE = ITEMS.registerSimpleItem("oil_bottle");

    public static final DeferredItem<Hammer> WOODEN_HAMMER = ITEMS.registerItem("wooden_hammer",
            (properties) -> new Hammer(TripleAreaToolMaterial.WOOD, properties));
    public static final DeferredItem<Hammer> STONE_HAMMER = ITEMS.registerItem("stone_hammer",
            (properties) -> new Hammer(TripleAreaToolMaterial.STONE, properties));
    public static final DeferredItem<Hammer> IRON_HAMMER = ITEMS.registerItem("iron_hammer",
            (properties) -> new Hammer(TripleAreaToolMaterial.IRON, properties));
    public static final DeferredItem<Hammer> GOLDEN_HAMMER = ITEMS.registerItem("golden_hammer",
            (properties) -> new Hammer(TripleAreaToolMaterial.GOLD, properties));
    public static final DeferredItem<Hammer> DIAMOND_HAMMER = ITEMS.registerItem("diamond_hammer",
            (properties) -> new Hammer(TripleAreaToolMaterial.DIAMOND, properties));


    public static void registerItems(IEventBus eBus) {
        ITEMS.register(eBus);
    }
}
