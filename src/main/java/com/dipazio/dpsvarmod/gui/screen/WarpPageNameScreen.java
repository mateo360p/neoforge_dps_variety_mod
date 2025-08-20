package com.dipazio.dpsvarmod.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class WarpPageNameScreen extends Screen {
    private Consumer<String> callback = null;
    private EditBox textField;
    private Button doneButton;

    public WarpPageNameScreen(Consumer<String> callback) {
        super(Component.translatable("gui.warp.bind"));
        this.callback = callback;
    }

    @Override
    protected void init() {
        this.textField = new EditBox(this.font, this.width / 2 - 150, 60, 300, 20, Component.literal(""));
        this.textField.setMaxLength(32);
        this.addWidget(textField);

        this.doneButton = this.addRenderableWidget(Button.builder(Component.translatable("gui.generic.done"), button -> {
            if (callback != null) callback.accept(textField.getValue());
            this.onClose();
        }).bounds(this.width / 2 - 100, this.height / 2 + 20, 200, 20).build());
        this.doneButton.active = false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawString(this.font, Component.translatable("gui.warp.bind_enter"),
                this.textField.getX(),
                textField.getY() - font.lineHeight - 2,
                10526880, false);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 16777215);

        this.textField.render(guiGraphics, mouseX, mouseY, partialTick);
        doneButton.active = (!textField.getValue().trim().isEmpty());
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

