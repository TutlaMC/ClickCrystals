package io.github.itzispyder.clickcrystals.modules.modules.rendering;

import io.github.itzispyder.clickcrystals.gui.misc.Color;
import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.ModuleSetting;
import io.github.itzispyder.clickcrystals.modules.modules.DummyModule;
import io.github.itzispyder.clickcrystals.modules.settings.SettingSection;

public class BlockOutline extends DummyModule {

    private final SettingSection scGeneral = getGeneralSection();
    public final ModuleSetting<Color> color = scGeneral.add(createColorSetting()
            .name("color")
            .description("Color of the block outline.")
            .def(0xFF00B7FF)
            .build()
    );

    public BlockOutline() {
        super("block-outline", Categories.RENDER, "Change the color of the block outline");
    }
}
