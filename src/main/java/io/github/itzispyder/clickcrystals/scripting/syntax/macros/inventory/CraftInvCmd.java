package io.github.itzispyder.clickcrystals.scripting.syntax.macros.inventory;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;

// @Format craft_inv 1 2 3 4
// You DONT need to be in a crafting table actually use it
public class CraftInvCmd extends ScriptCommand implements ThenChainable {
    public CraftInvCmd() {
        super("craft_inv");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (mc.gameMode == null || mc.player == null)
            return;
        if (!(mc.screen instanceof CraftingScreen screen)){ // cuz then it starts goofing around when you have both in same script
            AbstractContainerMenu menu = mc.player.containerMenu;
            InvUtils.craftCCS(menu, args, 4);
        }



        executeWithThen(args, 4);
    }
}
