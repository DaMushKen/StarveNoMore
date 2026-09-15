package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.util.PlumpAccess;
import net.damushken.starve_no_more.util.PlumpUtil;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityPlumpMixin {

    @Inject(method = "canEat", at = @At("HEAD"), cancellable = true)
    private void starvenomore$blockPlumpCanEat(CallbackInfoReturnable<Boolean> cir) {
        AnimalEntity self = (AnimalEntity)(Object) this;
        if (PlumpUtil.isEffectivelyPlump(self)) cir.setReturnValue(false);
    }

    @Inject(method = "lovePlayer", at = @At("HEAD"), cancellable = true)
    private void starvenomore$blockPlumpLovePlayer(PlayerEntity player, CallbackInfo ci) {
        AnimalEntity self = (AnimalEntity)(Object) this;
        if (PlumpUtil.isEffectivelyPlump(self)) ci.cancel();
    }
}
