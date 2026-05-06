package io.github.itzispyder.clickcrystals.scripting.syntax.macros.inventory;

import io.github.itzispyder.clickcrystals.scripting.ScriptArgs;
import io.github.itzispyder.clickcrystals.scripting.ScriptCommand;
import io.github.itzispyder.clickcrystals.scripting.syntax.ThenChainable;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;

// @Format craft 1 2 3 4 5 6 7 8 9
// You need to be in a crafting table actually use it
public class CraftCmd extends ScriptCommand implements ThenChainable {
    public CraftCmd() {
        super("craft");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (mc.gameMode == null || mc.player == null)
            return;


        AbstractContainerMenu menu = mc.player.containerMenu;
        if (mc.screen instanceof CraftingScreen screen) {
            InvUtils.craftCCS(menu, args, 9);
        }
        executeWithThen(args, 9);
    }
}

// sum mf said "i dont like crafting mods" - Server Owners probably