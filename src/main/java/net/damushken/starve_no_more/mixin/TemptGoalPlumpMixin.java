package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.util.PlumpAccess;
import net.damushken.starve_no_more.util.PlumpUtil;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.AgeableMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TemptGoal.class)
public abstract class TemptGoalPlumpMixin {

    @Shadow
    protected Mob mob;

    @Inject(method = "canUse", at = @At("RETURN"), cancellable = true)
    private void starvenomore$blockPlumpTempt(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        if (!(this.mob instanceof AgeableMob passive)) return;
        if (PlumpUtil.isEffectivelyPlump(passive)) cir.setReturnValue(false);
    }
}
