package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.util.PlumpUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class GoatDropsMixin {

    @Inject(method = "dropLoot", at = @At("TAIL"))
    private void starvenomore$goatExtraDrops(DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object) this;
        if (!(self instanceof GoatEntity goat)) return;
        if (!(self.getWorld() instanceof ServerWorld)) return;
        if (!ModConfig.get().doGoatsDropAndPlump) return;

        var random = self.getRandom();

        int mutton = 1 + random.nextInt(2); //1-2

        int string = random.nextInt(2); //0-1

        if (PlumpUtil.isEffectivelyPlump(goat) && ModConfig.get().doPlump == true) {

            self.dropStack(new ItemStack(
                    Items.MUTTON, (int) (mutton * ModConfig.get().plumpDropsMultiplier) +1
            ));

            if (string > 0) {
                self.dropStack(new ItemStack(
                        Items.STRING, (int) (string * ModConfig.get().plumpDropsMultiplier) +1
                ));
            }

        } else if (!PlumpUtil.isEffectivelyPlump(goat) || ModConfig.get().doPlump == false) {

            self.dropStack(new ItemStack(Items.MUTTON, mutton));

            if (string > 0) {
                self.dropStack(new ItemStack(Items.STRING, string));
            }
        }

    }
}
