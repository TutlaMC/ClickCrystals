package io.github.itzispyder.clickcrystals.modules.settings;

import io.github.itzispyder.clickcrystals.gui.elements.browsingmode.module.ColorSettingElement;
import io.github.itzispyder.clickcrystals.gui.misc.Color;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;

public class ColorSetting extends ModuleSetting<Color> {

    protected ColorSetting(String name, String description, Color def, Color val) {
        super(name, description, def, val);
    }

    public void setColor(int argb) {
        this.val = new Color(argb);
    }

    @Override
    public ColorSettingElement toGuiElement(int x, int y) {
        return new ColorSettingElement(this, x, y);
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder extends SettingBuilder<Color, Builder, ColorSetting> {

        public Builder def(int argb) {
            this.def = new Color(argb);
            return this;
        }

        public Builder val(int argb) {
            this.val = new Color(argb);
            return this;
        }

        @Override
        protected ColorSetting buildSetting() {
            return new ColorSetting(name, description, def, getOrDef(val, def));
        }
    }
}
