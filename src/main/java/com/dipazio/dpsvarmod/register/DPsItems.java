package com.dipazio.dpsvarmod.register;

import com.dipazio.dpsvarmod.DPsVarietyMod;
import com.dipazio.dpsvarmod.item._material.MegaToolMaterial;
import com.dipazio.dpsvarmod.item._material.VanillaToolMaterial;
import com.dipazio.dpsvarmod.item._parents.grand.DpFoodItem;
import com.dipazio.dpsvarmod.item.tools.Chopper;
import com.dipazio.dpsvarmod.item.tools.Digger;
import com.dipazio.dpsvarmod.item.tools.Hammer;
import com.dipazio.dpsvarmod.item.tools.Tiller;
import com.dipazio.dpsvarmod.item.warp.BoundWarpPage;
import com.dipazio.dpsvarmod.item.warp.PlayerWarpPage;
import com.dipazio.dpsvarmod.item.warp.WarpBook;
import com.dipazio.dpsvarmod.item.warp.UnboundWarpPage;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DPsItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(DPsVarietyMod.MODID);

    public static final DeferredItem<Item> WOODEN_DIGGER = ITEMS.registerItem("wooden_digger",
            (properties) -> new Digger(MegaToolMaterial.WOOD, 4.5F, 0.3F, properties).setBurnTime(200));
    public static final DeferredItem<Item> STONE_DIGGER = ITEMS.registerItem("stone_digger",
            (properties) -> new Digger(MegaToolMaterial.STONE, 5.5F, 0.3F, properties));
    public static final DeferredItem<Item> IRON_DIGGER = ITEMS.registerItem("iron_digger",
            (properties) -> new Digger(MegaToolMaterial.IRON, 6.5F, 0.3F, properties));
    public static final DeferredItem<Item> GOLDEN_DIGGER = ITEMS.registerItem("golden_digger",
            (properties) -> new Digger(MegaToolMaterial.GOLD, 4.5F, 0.3F, properties));
    public static final DeferredItem<Item> DIAMOND_DIGGER = ITEMS.registerItem("diamond_digger",
            (properties) -> new Digger(MegaToolMaterial.DIAMOND, 7.5F, 0.3F, properties));

    public static final DeferredItem<Item> WOODEN_HAMMER = ITEMS.registerItem("wooden_hammer",
            (properties) -> new Hammer(MegaToolMaterial.WOOD, 4.0F, 0.4F, properties).setBurnTime(200));
    public static final DeferredItem<Item> STONE_HAMMER = ITEMS.registerItem("stone_hammer",
            (properties) -> new Hammer(MegaToolMaterial.STONE, 5.0F, 0.4F, properties));
    public static final DeferredItem<Item> IRON_HAMMER = ITEMS.registerItem("iron_hammer",
            (properties) -> new Hammer(MegaToolMaterial.IRON, 6.0F, 0.4F, properties));
    public static final DeferredItem<Item> GOLDEN_HAMMER = ITEMS.registerItem("golden_hammer",
            (properties) -> new Hammer(MegaToolMaterial.GOLD, 4.0F, 0.4F, properties));
    public static final DeferredItem<Item> DIAMOND_HAMMER = ITEMS.registerItem("diamond_hammer",
            (properties) -> new Hammer(VanillaToolMaterial.DIAMOND, 7.0F, 0.4F, properties));

    public static final DeferredItem<Item> WOODEN_CHOPPER = ITEMS.registerItem("wooden_chopper",
            (properties) -> new Chopper(MegaToolMaterial.WOOD, 8, 8.0F, 0.2F, properties).setBurnTime(200));
    public static final DeferredItem<Item> STONE_CHOPPER = ITEMS.registerItem("stone_chopper",
            (properties) -> new Chopper(MegaToolMaterial.STONE, 16, 10.0F, 0.2F, properties));
    public static final DeferredItem<Item> IRON_CHOPPER = ITEMS.registerItem("iron_chopper",
            (properties) -> new Chopper(MegaToolMaterial.IRON, 32, 10.0F, 0.25F, properties));
    public static final DeferredItem<Item> GOLDEN_CHOPPER = ITEMS.registerItem("golden_chopper",
            (properties) -> new Chopper(MegaToolMaterial.GOLD, 8, 8.0F, 0.3F, properties)); // bruh
    public static final DeferredItem<Item> DIAMOND_CHOPPER = ITEMS.registerItem("diamond_chopper",
            (properties) -> new Chopper(MegaToolMaterial.DIAMOND, 64, 12.0F, 0.3F, properties));

    public static final DeferredItem<Item> WOODEN_TILLER = ITEMS.registerItem("wooden_tiller",
            (properties) -> new Tiller(MegaToolMaterial.WOOD, 4, 1.0F, 1.2F, properties).setBurnTime(200));
    public static final DeferredItem<Item> STONE_TILLER = ITEMS.registerItem("stone_tiller",
            (properties) -> new Tiller(MegaToolMaterial.STONE, 8, 1.15F, 2.0F, properties));
    public static final DeferredItem<Item> IRON_TILLER = ITEMS.registerItem("iron_tiller",
            (properties) -> new Tiller(MegaToolMaterial.IRON, 16, 1.3F, 3.0F, properties));
    public static final DeferredItem<Item> GOLDEN_TILLER = ITEMS.registerItem("golden_tiller",
            (properties) -> new Tiller(MegaToolMaterial.GOLD, 4, 1.0F, 1.0F, properties));
    public static final DeferredItem<Item> DIAMOND_TILLER = ITEMS.registerItem("diamond_tiller",
            (properties) -> new Tiller(MegaToolMaterial.DIAMOND, 32, 1.5F, 1.2F, properties));


    public static final DeferredItem<Item> COMBINED_ROTTEN_FLESH = ITEMS.registerItem("combined_rotten_flesh",
            (properties) -> new DpFoodItem(7, 0.28F, properties, DPsFoodsEffects.COMBINED_ROTTEN_FLESH));


    public static final DeferredItem<Item> WARP_BOOK = ITEMS.registerItem("warp_book",
            WarpBook::new);
    public static final DeferredItem<Item> UNBOUND_WARP_PAGE = ITEMS.registerItem("unbound_warp_page",
            UnboundWarpPage::new);
    public static final DeferredItem<Item> BOUND_WARP_PAGE = ITEMS.registerItem("bound_warp_page",
            BoundWarpPage::new);
    public static final DeferredItem<Item> PLAYER_WARP_PAGE = ITEMS.registerItem("player_warp_page",
            PlayerWarpPage::new);

    public static void registerItems(IEventBus eBus) {
        ITEMS.register(eBus);
    }
}
