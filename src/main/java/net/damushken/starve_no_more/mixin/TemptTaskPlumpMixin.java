package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.util.PlumpUtil;
import net.minecraft.entity.ai.brain.task.TemptTask;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TemptTask.class)
public abstract class TemptTaskPlumpMixin {

    @Inject(method = "shouldKeepRunning", at = @At("HEAD"), cancellable = true)
    private void starvenomore$blockPlumpShouldKeepRunning(ServerWorld serverWorld, PathAwareEntity pathAwareEntity,
                                                          long l, CallbackInfoReturnable<Boolean> cir) {
        if (pathAwareEntity instanceof PassiveEntity passive && PlumpUtil.isEffectivelyPlump(passive)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "run", at = @At("HEAD"), cancellable = true)
    private void starvenomore$blockPlumpRun(ServerWorld serverWorld, PathAwareEntity pathAwareEntity,
                                            long l, CallbackInfo ci) {
        if (pathAwareEntity instanceof PassiveEntity passive && PlumpUtil.isEffectivelyPlump(passive)) {
            ci.cancel();
        }
    }

    @Inject(method = "keepRunning", at = @At("HEAD"), cancellable = true)
    private void starvenomore$blockPlumpKeepRunning(ServerWorld serverWorld, PathAwareEntity pathAwareEntity,
                                                    long l, CallbackInfo ci) {
        if (pathAwareEntity instanceof PassiveEntity passive && PlumpUtil.isEffectivelyPlump(passive)) {
            ci.cancel();
        }
    }
}
