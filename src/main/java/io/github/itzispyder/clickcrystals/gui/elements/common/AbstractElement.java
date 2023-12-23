package io.github.itzispyder.clickcrystals.gui.elements.common;

import io.github.itzispyder.clickcrystals.gui.GuiElement;
import net.minecraft.client.util.math.MatrixStack;

public class AbstractElement extends GuiElement {

    private final RenderAction<AbstractElement> onRender;
    private final PressAction<AbstractElement> onPress;

    public AbstractElement(int x, int y, int width, int height, RenderAction<AbstractElement> onRender, PressAction<AbstractElement> pressAction, String tooltip, long tooltipDelay) {
        super(x, y, width, height);
        super.setTooltip(tooltip);
        super.setTooltipDelay(tooltipDelay);
        this.onRender = onRender;
        this.onPress = pressAction;
    }

    public AbstractElement(int x, int y, int width, int height, RenderAction<AbstractElement> onRender, PressAction<AbstractElement> pressAction) {
        this(x, y, width, height, onRender, pressAction, null, 500L);
    }

    @Override
    public void onRender(MatrixStack context, int mouseX, int mouseY) {
        this.onRender.onRender(context, mouseX, mouseY, this);
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        this.onPress.onPress(this);
    }

    public RenderAction<AbstractElement> getRenderAction() {
        return onRender;
    }

    public PressAction<AbstractElement> getPressAction() {
        return onPress;
    }

    public static Builder create() {
        return new Builder();
    }


    public static class Builder {
        private int x, y, width, height;
        private RenderAction<AbstractElement> onRender;
        private PressAction<AbstractElement> onPress;
        private String tooltip;
        private long tooltipDelay;

        public Builder() {
            onRender = (context, mouseX, mouseY, button) -> {};
            onPress = button -> {};
            x = y = width = height = 0;
            tooltip = null;
            tooltipDelay = 500L;
        }

        public Builder x(int x) {
            this.x = x;
            return this;
        }

        public Builder y(int y) {
            this.y = y;
            return this;
        }

        public Builder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder dimensions(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder onRender(RenderAction<AbstractElement> onRender) {
            this.onRender = onRender;
            return this;
        }

        public Builder onPress(PressAction<AbstractElement> onPress) {
            this.onPress = onPress;
            return this;
        }

        public Builder tooltip(String tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public Builder tooltipDelay(long tooltipDelay) {
            this.tooltipDelay = tooltipDelay;
            return this;
        }

        public AbstractElement build() {
            return new AbstractElement(x, y, width, height, onRender, onPress, tooltip, tooltipDelay);
        }
    }
}
