package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.util.PlumpAccess;
import net.minecraft.entity.Entity;
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
        if (!ModConfig.get().doPlump) return;
        if (self instanceof PlumpAccess plump && plump.starvenomore$isPlump()) {
            ci.cancel();
            if (!self.isSilent()) {
                self.getWorld().playSound(null, self.getX(), self.getY(), self.getZ(),
                        sound, self.getSoundCategory(),
                        volume * 1.5f, pitch * 0.75f);
            }
        }
    }
}
