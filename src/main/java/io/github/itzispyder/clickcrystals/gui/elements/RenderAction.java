package io.github.itzispyder.clickcrystals.gui.elements;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import net.minecraft.client.util.math.MatrixStack;

@FunctionalInterface
public interface RenderAction<T extends GuiElement> {

    void onRender(MatrixStack context, int mouseX, int mouseY, T button);
}
