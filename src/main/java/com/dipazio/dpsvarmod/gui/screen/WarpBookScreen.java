package com.dipazio.dpsvarmod.gui.screen;

import com.dipazio.dpsvarmod.DPsVarietyMod;
import com.dipazio.dpsvarmod.gui.menu.WarpBookMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WarpBookScreen extends AbstractContainerScreen<WarpBookMenu> {
    private static final ResourceLocation GUI_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(DPsVarietyMod.MODID, "textures/gui/warp_book/warp_book_gui.png");

    public WarpBookScreen(WarpBookMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(RenderType.GUI_TEXTURED, GUI_TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
        guiGraphics.blit(RenderType.GUI_TEXTURED, GUI_TEXTURE, x + 176, y, 176, 0, 36, 166, 256, 256); // bruh
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
