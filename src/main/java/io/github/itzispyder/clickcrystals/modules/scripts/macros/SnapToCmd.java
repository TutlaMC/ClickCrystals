package io.github.itzispyder.clickcrystals.modules.scripts.macros;

import io.github.itzispyder.clickcrystals.client.clickscript.ScriptArgs;
import io.github.itzispyder.clickcrystals.client.clickscript.ScriptCommand;
import io.github.itzispyder.clickcrystals.modules.scripts.syntax.OnEventCmd;
import io.github.itzispyder.clickcrystals.util.minecraft.PlayerUtils;
import io.github.itzispyder.clickcrystals.util.misc.CameraRotator;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

public class SnapToCmd extends ScriptCommand {

    public SnapToCmd() {
        super("snap_to");
    }

    @Override
    public void onCommand(ScriptCommand command, String line, ScriptArgs args) {
        if (PlayerUtils.playerNull()) {
            return;
        }

        // ex.      turn_to nearest_entity :creeper then say Yo
        Vec3d eyes = PlayerUtils.player().getEyePos();

        switch (args.get(0).toEnum(TurnToCmd.Mode.class, null)) {
            case NEAREST_BLOCK -> {
                Predicate<BlockState> filter = OnEventCmd.parseBlockPredicate(args.get(1).toString());
                PlayerUtils.runOnNearestBlock(32, filter, (pos, state) -> specifiedSnap(pos.toCenterPos(), eyes, args));
            }
            case NEAREST_ENTITY -> {
                Predicate<Entity> filter = OnEventCmd.parseEntityPredicate(args.get(1).toString());
                PlayerUtils.runOnNearestEntity(32, filter, entity -> specifiedSnap(entity instanceof LivingEntity le ? le.getEyePos() : entity.getPos(), eyes, args));
            }

            case ANY_BLOCK -> PlayerUtils.runOnNearestBlock(32, (pos, state) -> true, (pos, state) -> singleSnap(pos.toCenterPos(), eyes, args));
            case ANY_ENTITY -> PlayerUtils.runOnNearestEntity(32, Entity::isAlive, entity -> {
                singleSnap(entity instanceof LivingEntity le ? le.getEyePos() : entity.getPos(), eyes, args);
            });
        }
    }

    private void singleSnap(Vec3d dest, Vec3d camPos, ScriptArgs args) {
        Vec3d target = dest.subtract(camPos).normalize();
        CameraRotator.Goal goal = new CameraRotator.Goal(target);

        PlayerUtils.player().setPitch(goal.getPitch());
        PlayerUtils.player().setYaw(goal.getYaw());

        if (args.match(1, "then")) {
            args.executeAll(2);
        }
    }

    private void specifiedSnap(Vec3d dest, Vec3d camPos, ScriptArgs args) {
        Vec3d target = dest.subtract(camPos).normalize();
        CameraRotator.Goal goal = new CameraRotator.Goal(target);

        PlayerUtils.player().setPitch(goal.getPitch());
        PlayerUtils.player().setYaw(goal.getYaw());

        if (args.match(2, "then")) {
            args.executeAll(3);
        }
    }
}
