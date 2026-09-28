package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.util.PlumpAccess;
import net.damushken.starve_no_more.util.PlumpUtil;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Animal.class)
public abstract class AnimalPlumpMixin {

    @Inject(method = "canFallInLove", at = @At("HEAD"), cancellable = true)
    private void starvenomore$blockPlumpCanEat(CallbackInfoReturnable<Boolean> cir) {
        Animal self = (Animal)(Object) this;
        if (PlumpUtil.isEffectivelyPlump(self)) cir.setReturnValue(false);
    }

    @Inject(method = "setInLove", at = @At("HEAD"), cancellable = true)
    private void starvenomore$blockPlumpLovePlayer(Player player, CallbackInfo ci) {
        Animal self = (Animal)(Object) this;
        if (PlumpUtil.isEffectivelyPlump(self)) ci.cancel();
    }
}
