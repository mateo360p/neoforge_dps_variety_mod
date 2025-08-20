package com.dipazio.dpsvarmod.gui.screen;

import com.dipazio.dpsvarmod.packet.PacketHandler;
import com.dipazio.dpsvarmod.packet.packets.warp.TeleportPacket;
import com.dipazio.dpsvarmod.packet.packets.warp.TeleportToPlayerPacket;
import com.dipazio.dpsvarmod.register.DPsItems;
import com.dipazio.dpsvarmod.util.ItemsFuncs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class WarpBookTeleportScreen extends Screen {
    private final ItemStackHandler items;

    public WarpBookTeleportScreen(ItemStackHandler bookItems) {
        super(Component.translatable( "gui.warp.book_teleport"));
        this.items = bookItems;
    }

    @Override
    protected void init() {
        Button closeButton = this.addRenderableWidget(Button.builder(Component.translatable("gui.generic.close"), button -> {
            this.onClose();
        }).bounds(this.width / 2 - 100, this.height - 40, 200, 20).build());
        closeButton.active = true;

        int id = -1;
        for(int slot = 1; slot < this.items.getSlots(); ++slot) {
            ItemStack stack = items.getStackInSlot(slot);
            if (stack.isEmpty()) continue;

            CompoundTag data = ItemsFuncs.getData(stack);
            id++;

            try {
                boolean isPlayerPage = stack.getItem().equals(DPsItems.PLAYER_WARP_PAGE.get());

                this.addRenderableWidget(Button.builder(Component.literal(data.getString(isPlayerPage ? "player_name" : "waypoint_name")),
                    isPlayerPage ?
                        button -> {
                            TeleportToPlayerPacket packet = new TeleportToPlayerPacket(
                                    data.getString("uuid"));
                            PacketHandler.sendToServer(packet);
                            this.onClose();
                        } :
                        button -> {
                            TeleportPacket packet = new TeleportPacket(
                                    data.getDouble("tp_X"),
                                    data.getDouble("tp_Y"),
                                    data.getDouble("tp_Z"),
                                    data.getDouble("rot_X"),
                                    data.getDouble("rot_Y"),
                                    data.getString("dimension"));
                            PacketHandler.sendToServer(packet);
                            this.onClose();
                        }
                ).bounds((this.width - 404) / 2 + id % 6 * 68, 16 + 24 * (id / 6), 64, 16).build());
            } catch (Exception ignored) {} // just in case
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 5, 16777215);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}

