package com.dipazio.dpsvarmod.util;

import com.dipazio.dpsvarmod.DPsVarietyMod;
import com.dipazio.dpsvarmod.item.warp.UnboundWarpPage;
import com.dipazio.dpsvarmod.item.warp.WarpBook;
import com.dipazio.dpsvarmod.register.DPsItems;
import com.dipazio.dpsvarmod.util.warp.WarpDeathData;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@EventBusSubscriber(modid = DPsVarietyMod.MODID)
public class Events {
    @SubscribeEvent
    public static void onHurt(LivingDamageEvent.@NotNull Post event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            Level level = player.level();
            if (event.getSource() != level.damageSources().fellOutOfWorld() && player.getHealth() <= event.getOriginalDamage()) {
                if (!player.getInventory().contains((stack) -> stack.is(DPsItems.WARP_BOOK) && getDeathPages(stack, level) > 0)) {
                    return;
                }

                for(NonNullList<ItemStack> list : List.of(player.getInventory().items, player.getInventory().offhand)) {
                    for (ItemStack stack : list) {
                        if (stack.is(DPsItems.WARP_BOOK) && (getDeathPages(stack, level)) > 0) {
                            decreaseDeathPages(stack, level);
                            if (!level.isClientSide()) {
                                WarpDeathData data = WarpDeathData.get();
                                data.setDeathLocation(player.getUUID(), player.level().dimension(), player);
                            }
                            return;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(@NotNull PlayerEvent.@NotNull PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (!event.isEndConquered() && !player.level().isClientSide()) {
            WarpDeathData data = WarpDeathData.get();

            CompoundTag death = data.useAndRemove(player.getUUID());
            if (death == null) return;

            ItemStack single = new ItemStack(DPsItems.BOUND_WARP_PAGE.get());
            UnboundWarpPage.saveTeleport(
                    single,
                    death.getDouble("tp_X"),
                    death.getDouble("tp_Y"),
                    death.getDouble("tp_Z"),
                    player,
                    "Death Point",
                    death.getString("dimension")
            );

            if (!player.addItem(single)) {
                player.drop(single, false);
            }
        }
    }

    public static int getDeathPages(ItemStack stack, Level level) {
        if (!ItemsFuncs.hasData(stack, "inventory")) return 0;

        ItemStackHandler handler = ((WarpBook) stack.getItem()).getInventory(stack, level.registryAccess());
        return handler.getStackInSlot(0).getCount();
    }

    public static void decreaseDeathPages(ItemStack stack, Level level) {
        if (!ItemsFuncs.hasData(stack, "inventory")) return;

        ItemStackHandler handler = ((WarpBook) stack.getItem()).getInventory(stack, level.registryAccess());
        ItemStack slot = handler.getStackInSlot(0);
        slot.shrink(1);
        handler.setStackInSlot(0, slot);
    }
}
