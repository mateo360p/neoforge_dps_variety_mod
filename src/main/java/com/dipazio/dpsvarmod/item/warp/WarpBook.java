package com.dipazio.dpsvarmod.item.warp;

import com.dipazio.dpsvarmod.gui.menu.WarpBookMenu;
import com.dipazio.dpsvarmod.gui.screen.WarpBookTeleportScreen;
import com.dipazio.dpsvarmod.register.DPsItems;
import com.dipazio.dpsvarmod.util.ItemsFuncs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

public class WarpBook extends Item {

    public WarpBook(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        int slot = (hand == InteractionHand.MAIN_HAND) ? player.getInventory().selected : Inventory.SLOT_OFFHAND;

        if (!level.isClientSide()) {
            if (player.isShiftKeyDown()) {
                player.openMenu(new SimpleMenuProvider(
                        (id, inv, p) -> new WarpBookMenu(id, inv, slot),
                        Component.translatable("gui.warp.book_inventory")),
                        buf -> buf.writeVarInt(slot));
            }
        } else {
            if (!player.isShiftKeyDown()) {
                ItemStack stack = player.getInventory().getItem(slot);
                Minecraft.getInstance().setScreen(new WarpBookTeleportScreen(getInventory(
                        stack, player.level().registryAccess())
                ));
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        int slots;
        Component text;

        try { slots = getNotEmptySlots(stack, context.level());
        } catch (Exception var) { slots = 0;}

        if (slots > 0) {
            text = Component.translatable("info.warp.binds_num").append("" + slots).withStyle(ChatFormatting.GRAY);
        } else text = Component.translatable("info.warp.binds_zero").withStyle(ChatFormatting.GRAY);
        tooltipComponents.add(text);
    }
    //--------------------------------Inventory-----------------------------------
    int getNotEmptySlots(ItemStack stack, Level level) {
        ItemStackHandler inventory = getInventory(stack, level.registryAccess());
        int slots = inventory.getSlots();
        int notEmptySlots = 0;

        for(int slot = 1; slot < slots; ++slot) {
            ItemStack daStack = inventory.getStackInSlot(slot);
            if (!daStack.isEmpty()) notEmptySlots++;
        }
        return notEmptySlots;
    }

    boolean hasInventoryData(CompoundTag data) {
        return data.contains("inventory");
    }

    public ItemStackHandler getInventory(ItemStack stack, HolderLookup.Provider provider) {
        ItemStackHandler handler = new ItemStackHandler(28) {
            @Override
            protected void onContentsChanged(int slot) {
                saveInventory(stack, this, provider);
            }

            @Override
            protected int getStackLimit(int slot, ItemStack stack) {
                if (slot == 0) return 16; // Death page
                return 1;
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot == 0) return (stack.getItem() == DPsItems.DEATH_WARP_PAGE.get());

                return (stack.getItem() == DPsItems.BOUND_WARP_PAGE.get() ||
                        stack.getItem() == DPsItems.PLAYER_WARP_PAGE.get());
            }
        };

        loadInventory(stack, handler, provider);
        return handler;
    }

    public void saveInventory(ItemStack stack, ItemStackHandler handler, HolderLookup.Provider provider) {
        CompoundTag tag = ItemsFuncs.getData(stack);
        tag.put("inventory", handler.serializeNBT(provider));

        ItemsFuncs.saveData(stack, tag);
    }

    public void loadInventory(ItemStack stack, ItemStackHandler handler, HolderLookup.Provider provider) {
        if (hasInventoryData(ItemsFuncs.getData(stack))) {
            handler.deserializeNBT(provider, ItemsFuncs.getData(stack).getCompound("inventory"));
        }
    }

    public static boolean doFancyTeleport(
            ServerPlayer player,
            ResourceKey<Level> dimension,
            double toX,
            double toY,
            double toZ,
            double rotX,
            double rotY
    ) {
        ServerLevel newLevel;
        try { // idk
            if (dimension == null) return false;
            newLevel = player.getServer().getLevel(dimension);
            if (newLevel == null) return false;
        } catch (Exception ignored) {
            return false;
        }

        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();

        // Leaves
        Level oldLevel = player.level();
        if (!oldLevel.isClientSide()) {
            ((ServerLevel) oldLevel).sendParticles(
                    ParticleTypes.PORTAL,
                    true,
                    true,
                    x + 0.5,
                    y + 1.0,
                    z + 0.5,
                    150,
                    0.5,
                    0.25,
                    0.5,
                    1.5
            );
        }

        double traveledDistance = (!oldLevel.dimension().equals(dimension)) ?
                Double.POSITIVE_INFINITY :
                Math.sqrt(sqr(toX - x) + sqr(toY - y) + sqr(toZ - z));

        try {
            player.teleport(new TeleportTransition(newLevel, new Vec3(toX, toY, toZ), Vec3.ZERO, (float) rotY, (float) rotX, TeleportTransition.DO_NOTHING));
            player.causeFoodExhaustion((float) calcWarpHungerExhaustion(traveledDistance, player.level().getDifficulty()));
        } catch(Exception dummy) {
            System.out.println("Error on teleport: " + dummy); // idk
            return false;
        }

        // Arrives
        if (!newLevel.isClientSide()) {
            newLevel.sendParticles(
                    ParticleTypes.LARGE_SMOKE,
                    true,
                    true,
                    toX,
                    toY + 1.0,
                    toZ,
                    250,
                    0.5,
                    1.0,
                    0.5,
                    0.1
            );

            newLevel.sendParticles(
                    ParticleTypes.PORTAL,
                    true,
                    true,
                    toX + 0.5,
                    toY + 1.0,
                    toZ + 0.5,
                    140,
                    0.0,
                    1.0,
                    0.0,
                    1.0
            );
        }
        return true;
    }

    public static void sendCloudParticles(ServerPlayer player) {
        ServerLevel level = (ServerLevel) player.level();
        level.sendParticles(
                ParticleTypes.CLOUD,
                true,
                true,
                player.getX(),
                player.getY() + 0.5,
                player.getZ(),
                15,
                0.0,
                0.0,
                0.0,
                0.25
        );
    }

    public static double calcWarpHungerExhaustion(double distance, Difficulty difficulty) {
        distance = Mth.clamp(distance, 250.0, 20000.0);
        double distTax = distance * 0.005;
        double diffTax = switch (difficulty) {
            case EASY -> 1.0;
            case HARD -> 2.25;
            case PEACEFUL -> 0.0;
            default -> 1.75; // NORMAL and others idk
        };

        return distTax * diffTax;
    }


    // XD
    public static double sqr(double x) {
        return x * x;
    }
}