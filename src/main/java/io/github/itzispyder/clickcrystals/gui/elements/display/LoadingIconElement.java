package io.github.itzispyder.clickcrystals.gui.elements.display;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

import java.util.concurrent.atomic.AtomicInteger;

public class LoadingIconElement extends GuiElement {

    private final AtomicInteger rotation = new AtomicInteger(0);

    public LoadingIconElement(int x, int y, int size) {
        super(x, y, size, size);
        system.scheduler.runRepeatingTask(rotation::getAndIncrement, 0, 1, 3000);
    }

    @Override
    public void onRender(MatrixStack context, int mouseX, int mouseY) {
        context.push();
        context.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation.get()), x + width / 2.0F, y + height / 2.0F, 0);
        RenderUtils.drawTexture(context, Tex.Icons.LOADING, x, y, width, height);
        context.pop();
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {

    }
}
