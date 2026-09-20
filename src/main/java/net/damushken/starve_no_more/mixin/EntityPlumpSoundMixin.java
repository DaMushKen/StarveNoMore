package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.util.PlumpAccess;
import net.damushken.starve_no_more.util.PlumpUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityPlumpSoundMixin {

    @Inject(method = "playSound", at = @At("HEAD"), cancellable = true)
    private void starvenomore$lowerPlumpSound(SoundEvent sound, float volume, float pitch, CallbackInfo ci) {
        Entity self = (Entity)(Object) this;
        if (self.getWorld().isClient) return;

        if (!(self instanceof PassiveEntity passive)) return;
        if (!PlumpUtil.isEffectivelyPlump(passive)) return;

        ci.cancel();
        if (!self.isSilent()) {
            self.getWorld().playSound(null, self.getX(), self.getY(), self.getZ(),
                    sound, self.getSoundCategory(), volume * 0.5f, pitch * 0.65f);
        }
    }
}
