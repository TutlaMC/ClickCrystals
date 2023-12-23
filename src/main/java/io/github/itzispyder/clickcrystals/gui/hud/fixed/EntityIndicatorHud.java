package io.github.itzispyder.clickcrystals.gui.hud.fixed;

import io.github.itzispyder.clickcrystals.gui.hud.Hud;
import io.github.itzispyder.clickcrystals.gui.misc.Tex;
import io.github.itzispyder.clickcrystals.gui.misc.brushes.MobHeadBrush;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.modules.modules.rendering.EntityIndicator;
import io.github.itzispyder.clickcrystals.util.minecraft.RenderUtils;
import io.github.itzispyder.clickcrystals.util.misc.Voidable;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;

public class EntityIndicatorHud extends Hud {

    public EntityIndicatorHud() {
        super("entity-indicator-hud");
        this.setFixed(true);
    }

    @Override
    public void render(MatrixStack context) {
        EntityIndicator m = Module.get(EntityIndicator.class);
        if (!m.isEnabled()) {
            return;
        }
        if (m.updatePerRender.getVal()) {
            m.update();
        }

        int cX = RenderUtils.winWidth() / 2;
        int cY = RenderUtils.winHeight() / 2;
        int radius = m.hudSize.getVal();
        int size = m.spriteSize.getVal();
        var nearest = Voidable.of(EntityIndicator.nearest);

        nearest.accept(display -> {
            context.push();
            context.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(display.rotToYaw()), cX, cY, 0);
            RenderUtils.drawTexture(context, Tex.Overlays.DIRECTION, cX - radius, cY - radius, radius * 2, radius * 2);
            context.pop();
        });

        Voidable.of(EntityIndicator.entities).accept(displays -> {
            for (EntityIndicator.EntityDisplay display : displays) {
                float θ = display.rotToYaw(); // TheTrouper gave me this Unicode LOL (the real theta)
                int x = (int)(cX + Math.cos(Math.toRadians(θ - 90)) * radius);
                int y = (int)(cY + Math.sin(Math.toRadians(θ - 90)) * radius);
                MobHeadBrush.drawHead(context, display.entity(), x - size / 2, y - size / 2, size);
            }
        });

        nearest.accept(display -> {
            float θ = display.rotToYaw(); // TheTrouper gave me this Unicode LOL (the real theta)
            int x = (int)(cX + Math.cos(Math.toRadians(θ - 90)) * radius);
            int y = (int)(cY + Math.sin(Math.toRadians(θ - 90)) * radius);
            int bigger = size + 2;

            RenderUtils.fill(context, x - bigger / 2 - 1, y - bigger / 2 - 1, bigger + 2, bigger + 2, 0xFFFFFFFF);
            MobHeadBrush.drawHead(context, display.entity(), x - bigger / 2, y - bigger / 2, bigger);
        });
    }
}
