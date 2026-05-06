package io.github.itzispyder.clickcrystals.modules.modules.clickcrystals;

import io.github.itzispyder.clickcrystals.gui.misc.Color;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

public class SelfGlow extends DummyModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Color> glowColor = scGeneral.add(createColorSetting()
            .name("glow-color-setting")
            .description("Set the color of the glowing effect")
            .def(0xFF00FFFF)
            .build());

    public SelfGlow() {
        super("self-glow", Categories.CLIENT, "Am I Glowing?");
    }
}
