package com.dipazio.dpsvarmod.item.warp;

import com.dipazio.dpsvarmod.gui.menu.WarpBookMenu;
import com.dipazio.dpsvarmod.gui.screen.WarpBookTeleportScreen;
import com.dipazio.dpsvarmod.register.DPsItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

public class WarpBook extends Item {

    public WarpBook(Properties properties) {
        super(properties.stacksTo(1));
    }

    public ItemStackHandler getInventory(ItemStack stack, HolderLookup.Provider provider) {
        ItemStackHandler handler = new ItemStackHandler(28) {
            @Override
            protected void onContentsChanged(int slot) {
                saveInventory(stack, this, provider);
            }

            @Override
            protected int getStackLimit(int slot, ItemStack stack) {
                return 1;
            }

            @Override
            public boolean isItemValid(int slot, ItemStack stack) {
                if (slot == 0) return false; // Future Death Paper
                return (stack.getItem() == DPsItems.BOUND_WARP_PAGE.get());
            }
        };

        loadInventory(stack, handler, provider);
        return handler;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        int slot = (hand == InteractionHand.MAIN_HAND)
                ? player.getInventory().selected
                : Inventory.SLOT_OFFHAND;

        if (!level.isClientSide()) {
            if (player.isShiftKeyDown()) {
                player.openMenu(new SimpleMenuProvider(
                        (id, inv, p) -> new WarpBookMenu(id, inv, slot),
                        Component.translatable("gui.warp_book")
                ), buf -> buf.writeVarInt(slot));
            }
        } else {
            if (!player.isShiftKeyDown()) {
                ItemStack stack = player.getInventory().getItem(slot);
                Minecraft.getInstance().setScreen(new WarpBookTeleportScreen(getInventory(
                        stack,
                        player.level().registryAccess())
                ));
            }
        }

        return InteractionResult.SUCCESS;
    }

    public void saveInventory(ItemStack stack, ItemStackHandler handler, HolderLookup.Provider provider) {
        CompoundTag tag = getData(stack);
        tag.put("inventory", handler.serializeNBT(provider));

        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public void loadInventory(ItemStack stack, ItemStackHandler handler, HolderLookup.Provider provider) {
        if (hasInventoryData(getData(stack))) {
            handler.deserializeNBT(provider, getData(stack).getCompound("inventory"));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        int slots;
        try {
            slots = getNotEmptySlots(stack, context.level());
        } catch (Exception var) {
            slots = 0;
        }
        Component text;

        if (slots > 0) {
            text = Component.literal("#Binds: " + slots).withStyle(ChatFormatting.GRAY);
        } else text = Component.literal("No Binds, use while shifting to bound pages").withStyle(ChatFormatting.GRAY);
        tooltipComponents.add(text);
    }

    public int getNotEmptySlots(ItemStack stack, Level level) {
        ItemStackHandler inventory = getInventory(stack, level.registryAccess());
        int slots = inventory.getSlots();
        int notEmptySlots = 0;

        for(int slot = 1; slot < slots; ++slot) {
            ItemStack daStack = inventory.getStackInSlot(slot);
            if (!daStack.isEmpty()) notEmptySlots++;
        }
        return notEmptySlots;
    }

    public boolean hasInventoryData(CompoundTag data) {
        return data.contains("inventory");
    }

    public CompoundTag getData(ItemStack stack) {
        return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    }

    public boolean hasTpData(CompoundTag data) {
        return data.contains("tp_X") && data.contains("tp_Y") && data.contains("tp_Z");
    }
}