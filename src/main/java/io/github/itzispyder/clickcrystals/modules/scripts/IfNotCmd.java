package io.github.itzispyder.clickcrystals.modules.scripts;

import io.github.itzispyder.clickcrystals.Global;
import io.github.itzispyder.clickcrystals.client.clickscript.ClickScript;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.util.minecraft.HotbarUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.InvUtils;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;

import java.util.function.Predicate;

public class IfNotCmd extends ScriptCommand implements Global {

    public IfNotCmd() {
        super("if_not");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        IfCmd.ConditionType type = args.get(0).enumValue(IfCmd.ConditionType.class, null);

        if (mc == null || PlayerUtils.playerNull()) {
            return;
        }

        switch (type) {
            case HOLDING -> {
                if (!OnEventCmd.parseItemPredicate(args.get(1).stringValue()).test(HotbarUtils.getHand())) {
                    ClickScript.executeSingle(args.getAll(2).stringValue());
                }
            }
            case OFF_HOLDING -> {
                if (!OnEventCmd.parseItemPredicate(args.get(1).stringValue()).test(HotbarUtils.getHand(Hand.OFF_HAND))) {
                    ClickScript.executeSingle(args.getAll(2).stringValue());
                }
            }
            case TARGET_BLOCK -> {
                if (mc.crosshairTarget instanceof BlockHitResult hit) {
                    if (!OnEventCmd.parseBlockPredicate(args.get(1).stringValue()).test(PlayerUtils.getWorld().getBlockState(hit.getBlockPos()))) {
                        ClickScript.executeSingle(args.getAll(2).stringValue());
                    }
                }
            }
            case TARGET_ENTITY -> {
                if (mc.crosshairTarget instanceof EntityHitResult hit) {
                    if (!OnEventCmd.parseEntityPredicate(args.get(1).stringValue()).test(hit.getEntity())) {
                        ClickScript.executeSingle(args.getAll(2).stringValue());
                    }
                }
            }
            case INVENTORY_HAS -> {
                if (!InvUtils.has(OnEventCmd.parseItemPredicate(args.get(1).stringValue()))) {
                    ClickScript.executeSingle(args.getAll(2).stringValue());
                }
            }
            case HOTBAR_HAS -> {
                if (!HotbarUtils.has(OnEventCmd.parseItemPredicate(args.get(1).stringValue()))) {
                    ClickScript.executeSingle(args.getAll(2).stringValue());
                }
            }
            case INPUT_ACTIVE -> {
                if (!args.get(1).enumValue(InputCmd.Action.class, null).isActive()) {
                    ClickScript.executeSingle(args.getAll(2).stringValue());
                }
            }
            case BLOCK_IN_RANGE -> {
                Predicate<BlockState> filter = args.match(1, "any_block") ? state -> true : OnEventCmd.parseBlockPredicate(args.get(1).stringValue());
                double range = args.get(2).doubleValue();
                boolean result = PlayerUtils.runOnNearestBlock(range, filter, (pos, state) -> {
                    if (pos.toCenterPos().distanceTo(PlayerUtils.getPos()) > range) {
                        ClickScript.executeSingle(args.getAll(3).stringValue());
                    }
                });
                if (!result) {
                    ClickScript.executeSingle(args.getAll(3).stringValue());
                }
            }
            case ENTITY_IN_RANGE -> {
                Predicate<Entity> filter = args.match(1, "any_entity") ? entity -> true : OnEventCmd.parseEntityPredicate(args.get(1).stringValue());
                double range = args.get(2).doubleValue();
                boolean result = PlayerUtils.runOnNearestEntity(range, filter, entity -> {
                    if (entity.distanceTo(PlayerUtils.player()) > range) {
                        ClickScript.executeSingle(args.getAll(3).stringValue());
                    }
                });
                if (!result) {
                    ClickScript.executeSingle(args.getAll(3).stringValue());
                }
            }
            case ATTACK_PROGRESS -> {
                if (!IfCmd.evalIntegers(PlayerUtils.player().getAttackCooldownProgress(1.0F), args.get(1).stringValue())) {
                    ClickScript.executeSingle(args.getAll(2).stringValue());
                }
            }
            case HEALTH -> {
                if (!IfCmd.evalIntegers((int)PlayerUtils.player().getHealth(), args.get(1).stringValue())) {
                    ClickScript.executeSingle(args.getAll(2).stringValue());
                }
            }
            case ARMOR -> {
                if (!IfCmd.evalIntegers(PlayerUtils.player().getArmor(), args.get(1).stringValue())) {
                    ClickScript.executeSingle(args.getAll(2).stringValue());
                }
            }
            case POS_X -> {
                if (!IfCmd.evalIntegers((int)PlayerUtils.getPos().getX(), args.get(1).stringValue())) {
                    ClickScript.executeSingle(args.getAll(2).stringValue());
                }
            }
            case POS_Y -> {
                if (!IfCmd.evalIntegers((int)PlayerUtils.getPos().getY(), args.get(1).stringValue())) {
                    ClickScript.executeSingle(args.getAll(2).stringValue());
                }
            }
            case POS_Z -> {
                if (!IfCmd.evalIntegers((int)PlayerUtils.getPos().getZ(), args.get(1).stringValue())) {
                    ClickScript.executeSingle(args.getAll(2).stringValue());
                }
            }
        }
    }
}