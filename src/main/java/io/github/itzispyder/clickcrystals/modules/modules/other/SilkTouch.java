package io.github.itzispyder.clickcrystals.modules.modules.other;

import io.github.itzispyder.clickcrystals.modules.Categories;
import io.github.itzispyder.clickcrystals.modules.Module;
import io.github.itzispyder.clickcrystals.util.ChatUtils;

public class SilkTouch extends Module {

    public SilkTouch() {
        super("SilkTouch", Categories.OTHER,"\"Is there a silk touch module?\" - I_Got_You_Dead");
    }

    @Override
    protected void onEnable() {
        super.setEnabled(false);
        ChatUtils.sendChatMessage("I just made my weapon silk touch! This is not possible and will now crash my game.");
        System.exit(-1);
    }

    @Override
    protected void onDisable() {

    }
}