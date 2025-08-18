package com.dipazio.dpsvarmod.item.warp;

import com.dipazio.dpsvarmod.gui.screen.WarpPageNameScreen;
import com.dipazio.dpsvarmod.packet.packets.warp.BindPacket;
import com.dipazio.dpsvarmod.packet.PacketHandler;
import com.dipazio.dpsvarmod.register.DPsItems;
import com.dipazio.dpsvarmod.util.ItemsFuncs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class UnboundWarpPage extends Item {

    public UnboundWarpPage(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            ItemStack single = new ItemStack(DPsItems.BOUND_WARP_PAGE.get());
            single.setCount(1);

            Minecraft.getInstance().setScreen(new WarpPageNameScreen(name -> {
                BindPacket packet = new BindPacket(hand, name);
                PacketHandler.sendToServer(packet);
            }));
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        Component text = Component.literal("No teleport data, Use to save a position").withStyle(ChatFormatting.GRAY);
        tooltipComponents.add(text);
    }

    public static void saveTeleport(ItemStack stack, Player player, String waypointName) {
        CompoundTag data = ItemsFuncs.getData(stack);

        data.putDouble("tp_X", player.getX());
        data.putDouble("tp_Y", player.getY());
        data.putDouble("tp_Z", player.getZ());
        data.putString("waypoint_name", waypointName);

        ItemsFuncs.saveData(stack, data);
    }
}