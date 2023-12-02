package io.github.itzispyder.clickcrystals.gui.hud.fixed;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.misc.CrystPerSec;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.Window;
import net.minecraft.item.Items;

public class ClickPerSecondHud extends Hud {

    public ClickPerSecondHud() {
        super("cps-hud");
        this.setFixed(true);
    }

    @Override
    public void render(MatrixStack context) {
        Module cpsHud = Module.get(CrystPerSec.class);
        if (!cpsHud.isEnabled()) return;
        String text = "§f" + CrystPerSec.getCrystalPerSecond() + " §7c/s";

        final Window win = mc.getWindow();
        final int x = win.getScaledWidth() / 2;
        final int y = win.getScaledHeight() / 2;

        if (!HotbarUtils.isHolding(Items.END_CRYSTAL)) return;

        RenderUtils.drawCenteredText(context, text, x, y + 5, true);
    }
}