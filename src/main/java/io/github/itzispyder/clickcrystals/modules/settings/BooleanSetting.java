package io.github.itzispyder.clickcrystals.modules.settings;

import io.github.itzispyder.clickcrystals.gui.elements.cc.settings.BooleanSettingElement;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, String description, boolean def, boolean val) {
        super(name, description, def, val);
    }

    @Override
    public BooleanSettingElement toGuiElement(int x, int y, int width, int height) {
        return new BooleanSettingElement(this, x, y, width, height);
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder extends SettingBuilder<Boolean> {
        @Override
        public Setting<Boolean> build() {
            return new BooleanSetting(name, description, def, getOrDef(val, def));
        }
    }
}