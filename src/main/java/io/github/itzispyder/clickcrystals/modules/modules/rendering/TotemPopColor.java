package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.gui.misc.Color;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

public class TotemPopColor extends DummyModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Color> color = scGeneral.add(createColorSetting()
            .name("color")
            .description("The color used to swap totem pop")
            .def(Color.WHITE)
            .build());

    public TotemPopColor(){
        super("totem-color", Categories.RENDER, "Change totem pop particles color");
    }
}