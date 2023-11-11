package io.github.itzispyder.clickcrystals.events.listeners;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.events.EventHandler;
import io.github.itzispyder.clickcrystals.events.Listener;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickEndEvent;
import io.github.itzispyder.clickcrystals.events.events.world.ClientTickStartEvent;

public class TickEventListener implements Listener, Global {

    public static boolean shouldForward, shouldBackward, shouldStrafeLeft, shouldStrafeRight;

    @EventHandler
    public void onTickStart(ClientTickStartEvent e) {
        try {
            this.handleAutoKeys();
        }
        catch (Exception ignore) {};
    }

    @EventHandler
    public void onTickEnd(ClientTickEndEvent e) {

    }

    public static void forward(long millis) {
        if (!shouldForward) {
            shouldForward = true;
            system.scheduler.runDelayedTask(() -> {
                shouldForward = false;
                mc.options.forwardKey.setPressed(false);
            }, millis);
        }
    }

    public static void backward(long millis) {
        if (!shouldBackward) {
            shouldBackward = true;
            system.scheduler.runDelayedTask(() -> {
                shouldBackward = false;
                mc.options.backKey.setPressed(false);
            }, millis);
        }
    }

    public static void strafeLeft(long millis) {
        if (!shouldStrafeLeft) {
            shouldStrafeLeft = true;
            system.scheduler.runDelayedTask(() -> {
                shouldStrafeLeft = false;
                mc.options.leftKey.setPressed(false);
            }, millis);
        }
    }

    public static void strafeRight(long millis) {
        if (!shouldStrafeRight) {
            shouldStrafeRight = true;
            system.scheduler.runDelayedTask(() -> {
                shouldStrafeRight = false;
                mc.options.rightKey.setPressed(false);
            }, millis);
        }
    }

    private void handleAutoKeys() {
        if (shouldForward) {
            mc.options.forwardKey.setPressed(true);
        }
        if (shouldBackward) {
            mc.options.backKey.setPressed(true);
        }
        if (shouldStrafeLeft) {
            mc.options.leftKey.setPressed(true);
        }
        if (shouldStrafeRight) {
            mc.options.rightKey.setPressed(true);
        }
    }
}
