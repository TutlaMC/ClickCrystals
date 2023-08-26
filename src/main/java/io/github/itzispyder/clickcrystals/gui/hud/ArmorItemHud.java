package io.github.itzispyder.clickcrystals.gui.hud;

import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.ArmorHud;
import net.minecraft.client.gui.DrawContext;

public class ArmorItemHud extends Hud {

    public ArmorItemHud() {

    }

    @Override
    public void render(DrawContext context) {
        ArmorHud hud = Module.get(ArmorHud.class);

        if (hud.isEnabled()) {
            hud.onRender(context);
        }
    }
}
