package io.github.itzispyder.clickcrystals.gui.elements.browsingmode.module;

import io.github.itzispyder.clickcrystals.gui.elements.common.AbstractElement;
import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.ColorWheelElement;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.modules.settings.ColorSetting;
import io.github.itzispyder.clickcrystals.util.minecraft.render.RenderUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class ColorSettingElement extends SettingElement<ColorSetting> {

    private final ColorWheelElement colorPicker;

    public ColorSettingElement(ColorSetting setting, int x, int y) {
        super(setting, x, y);
        this.colorPicker = new ColorWheelElement(x + 169, y - 9, height / 20 * 9);
        this.colorPicker.setArgb(setting.getVal().getHex());
        this.addChild(colorPicker);

        createResetButton();
    }

    @Override
    public void onRender(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        this.renderSettingDetails(context);
        this.update();
    }

    @Override
    public void createResetButton() {
        int drawY = y + 7;
        int drawX = x + width - 5;

        this.addChild(AbstractElement.create()
                .pos(drawX, drawY)
                .dimensions(10, 10)
                .onPress(button -> {
                    this.revertSettingValue();
                    this.colorPicker.setArgb(setting.getVal().getHex());
                })
                .onRender((context, mouseX, mouseY, button) -> {
                    RenderUtils.drawTexture(context, Tex.Icons.RESET, button.x, button.y, button.width, button.height);
                })
                .build()
        );
    }

    private void update() {
        setting.setColor(colorPicker.getArgb());
    }

    public ColorSetting getSetting() {
        return setting;
    }
}
