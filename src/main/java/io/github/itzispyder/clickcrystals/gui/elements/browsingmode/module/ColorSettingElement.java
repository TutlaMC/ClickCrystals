package io.github.itzispyder.clickcrystals.gui.elements.browsingmode.module;

import io.github.itzispyder.clickcrystals.gui.elements.common.interactive.ColorWheelElement;
import io.github.itzispyder.clickcrystals.modules.settings.ColorSetting;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class ColorSettingElement extends SettingElement<ColorSetting> {

    private final ColorWheelElement colorPicker;

    public ColorSettingElement(ColorSetting setting, int x, int y) {
        super(setting, x, y);
        this.colorPicker = new ColorWheelElement(x + 169, y - 9, height / 20 * 9);
        this.colorPicker.setArgb(setting.getVal().getHex());
        this.addChild(colorPicker);
    }

    @Override
    public void onRender(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        this.renderSettingDetails(context);
        this.update();
    }

    private void update() {
        setting.setColor(colorPicker.getArgb());
    }

    public ColorSetting getSetting() {
        return setting;
    }
}
