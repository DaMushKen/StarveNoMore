package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.util.PlumpAccess;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TemptGoal.class)
public abstract class TemptGoalPlumpMixin {

    @Shadow
    protected PathAwareEntity mob;

    @Inject(method = "canStart", at = @At("RETURN"), cancellable = true)
    private void starvenomore$blockPlumpTempt(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        if (!ModConfig.get().doPlump) return;

        if (this.mob instanceof PlumpAccess plump && plump.starvenomore$isPlump()) {
            cir.setReturnValue(false);
        }
    }
}
