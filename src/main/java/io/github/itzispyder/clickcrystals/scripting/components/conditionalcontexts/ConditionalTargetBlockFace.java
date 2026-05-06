package io.github.itzispyder.clickcrystals.scripting.components.conditionalcontexts;

import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationContext;
import io.github.itzispyder.clickcrystals.scripting.components.ConditionEvaluationResult;
import io.github.itzispyder.clickcrystals.scripting.components.Conditional;
import io.github.itzispyder.clickcrystals.util.minecraft.EntityUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class ConditionalTargetBlockFace implements Conditional {

    @Override
    public ConditionEvaluationResult evaluate(ConditionEvaluationContext ctx) {
        HitResult hit = EntityUtils.getTarget(ctx.entity);
        if (!(hit instanceof BlockHitResult block))
            return ctx.end(true, false);

        Direction hitFace = block.getDirection();
        if (ctx.match(0, "side"))
            return ctx.end(true, hitFace.getAxis().isHorizontal());

        Direction direction = ctx.get(0).toEnum(Direction.class);
        return ctx.end(true, hitFace == direction);
    }
}
