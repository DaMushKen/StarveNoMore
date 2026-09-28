package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.util.PlumpUtil;
import net.minecraft.world.entity.ai.behavior.FollowTemptation;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FollowTemptation.class)
public abstract class FollowTemptationPlumpMixin {

    @Inject(method = "canStillUse", at = @At("HEAD"), cancellable = true)
    private void starvenomore$blockPlumpShouldKeepRunning(ServerLevel serverWorld, PathfinderMob pathAwareEntity,
                                                          long l, CallbackInfoReturnable<Boolean> cir) {
        if (pathAwareEntity instanceof AgeableMob passive && PlumpUtil.isEffectivelyPlump(passive)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "start", at = @At("HEAD"), cancellable = true)
    private void starvenomore$blockPlumpRun(ServerLevel serverWorld, PathfinderMob pathAwareEntity,
                                            long l, CallbackInfo ci) {
        if (pathAwareEntity instanceof AgeableMob passive && PlumpUtil.isEffectivelyPlump(passive)) {
            ci.cancel();
        }
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void starvenomore$blockPlumpKeepRunning(ServerLevel serverWorld, PathfinderMob pathAwareEntity,
                                                    long l, CallbackInfo ci) {
        if (pathAwareEntity instanceof AgeableMob passive && PlumpUtil.isEffectivelyPlump(passive)) {
            ci.cancel();
        }
    }
}
