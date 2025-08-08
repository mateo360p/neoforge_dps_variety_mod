package com.dipazio.dpsvarmod.item._material;

public class MegaToolMaterial {
    public static final DpToolMaterial WOOD;
    public static final DpToolMaterial STONE;
    public static final DpToolMaterial IRON;
    public static final DpToolMaterial DIAMOND;
    public static final DpToolMaterial GOLD;
    public static final DpToolMaterial NETHERITE;

    // Looks crazy, but actually you have less durability (I think-)
    static {
        WOOD = DpToolMaterial.copyFrom(VanillaToolMaterial.WOOD).setDurability(261);
        STONE = DpToolMaterial.copyFrom(VanillaToolMaterial.STONE).setDurability(581);
        IRON = DpToolMaterial.copyFrom(VanillaToolMaterial.IRON).setDurability(1110);
        DIAMOND = DpToolMaterial.copyFrom(VanillaToolMaterial.DIAMOND).setDurability(6930);
        GOLD = DpToolMaterial.copyFrom(VanillaToolMaterial.GOLD).setDurability(142);
        NETHERITE = DpToolMaterial.copyFrom(VanillaToolMaterial.NETHERITE).setDurability(9017);
    }
    // I have a python file with the "formula" to get these durabilities in the _assets folder :v
}
