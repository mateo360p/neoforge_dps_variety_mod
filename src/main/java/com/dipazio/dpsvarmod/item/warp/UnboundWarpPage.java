package com.dipazio.dpsvarmod.item.warp;

import com.dipazio.dpsvarmod.gui.screen.WarpPageNameScreen;
import com.dipazio.dpsvarmod.packet.packets.warp.BindPacket;
import com.dipazio.dpsvarmod.packet.PacketHandler;
import com.dipazio.dpsvarmod.util.ItemsFuncs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class UnboundWarpPage extends Item {

    public UnboundWarpPage(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            if (!player.isShiftKeyDown()) {
                Minecraft.getInstance().setScreen(new WarpPageNameScreen(name -> {
                    BindPacket packet = new BindPacket(hand, name, false);
                    PacketHandler.sendToServer(packet);
                }));
            } else {
                BindPacket packet = new BindPacket(hand, "itDoesntMatterIthinkNyeheheheh", true);
                PacketHandler.sendToServer(packet);
            }
            return InteractionResult.CONSUME;
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        Component text = Component.translatable("info.warp.binds_use").withStyle(ChatFormatting.GRAY);
        tooltipComponents.add(text);
    }

    public static void saveTeleport(ItemStack stack, Player player, String waypointName, ResourceKey<Level> dimension) {
        CompoundTag data = ItemsFuncs.getData(stack);
        ResourceLocation dim = dimension.location();

        data.putDouble("tp_X", player.getX());
        data.putDouble("tp_Y", player.getY());
        data.putDouble("tp_Z", player.getZ());
        data.putDouble("rot_X", player.getXRot());
        data.putDouble("rot_Y", player.getYRot());
        data.putString("dimension", dim.toString());
        data.putString("waypoint_name", waypointName);

        ItemsFuncs.saveData(stack, data);
    }

    public static void saveTeleport(ItemStack stack, double x, double y, double z, Player player, String waypointName, String dimension) {
        CompoundTag data = ItemsFuncs.getData(stack);

        data.putDouble("tp_X", x);
        data.putDouble("tp_Y", y);
        data.putDouble("tp_Z", z);
        data.putDouble("rot_X", player.getXRot());
        data.putDouble("rot_Y", player.getYRot());
        data.putString("dimension", dimension);
        data.putString("waypoint_name", waypointName);

        ItemsFuncs.saveData(stack, data);
    }

    public static void savePlayerTeleport(ItemStack stack, Player player) {
        CompoundTag data = ItemsFuncs.getData(stack);

        data.putString("player_name", player.getName().getString()); // Just visual
        data.putString("uuid", player.getStringUUID());

        ItemsFuncs.saveData(stack, data);
    }

    @Nullable
    public static ResourceKey<Level> returnDimension(String str) {
        ResourceLocation dimension = ResourceLocation.tryParse(str);
        if (dimension == null) return null;

        return ResourceKey.create(Registries.DIMENSION, dimension);
    }
}