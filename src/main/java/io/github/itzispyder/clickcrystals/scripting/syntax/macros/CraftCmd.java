package io.github.itzispyder.clickcrystals.modules.scripts.macros;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptParser;
import io.github.itzispyder.clickcrystals.modules.scripts.TargetType;
import io.github.itzispyder.clickcrystals.modules.scripts.ThenChainable;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.VectorParser;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.Entity;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

public class CraftCmd extends ScriptCommand implements ThenChainable {

    public CraftCmd() {
        super("craft");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (mc.interactionManager == null) {
            return;
        }
        Predicate<BlockState> filter = ScriptParser.parseBlockPredicate(args.get(0).toString());
        PlayerUtils.runOnNearestBlock(32, filter, (pos, state) -> {
                    Vec3d vector = PlayerUtils.getEyes().subtract(pos.toCenterPos());
                    Direction face = Direction.getFacing(vector);
                    BlockHitResult hit = new BlockHitResult(pos.toCenterPos(), face, pos, false);
                    ActionResult e = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, hit);
                    if (PlayerUtils.getWorld().getBlockState(hit.getBlockPos()).isOf(Blocks.CRAFTING_TABLE)){
                        if (mc.currentScreen instanceof HandledScreen<?> screen &&
                                screen.getScreenHandler() instanceof CraftingScreenHandler handler) {
                            System.out.println("E");
                            mc.interactionManager.clickSlot(
                                    handler.syncId,
                                    2,
                                    1,
                                    SlotActionType.PICKUP,
                                    mc.player
                            );
                        }
                    }
        });
        executeWithThen(args, 2);

        if (args.match(1, "then")) {
            args.executeAll(2);
        }
    }
}