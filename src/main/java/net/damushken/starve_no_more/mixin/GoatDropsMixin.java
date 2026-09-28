package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.util.PlumpUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class GoatDropsMixin {

    @Inject(method = "dropFromLootTable", at = @At("TAIL"))
    private void starvenomore$goatExtraDrops(ServerLevel world, DamageSource source, boolean causedByPlayer, CallbackInfo ci) {
        LivingEntity self = (LivingEntity)(Object) this;
        if (!(self instanceof Goat goat)) return;
        if (!(self.level() instanceof ServerLevel serverWorld)) return;
        if (!ModConfig.get().doGoatsDropAndPlump) return;

        var random = self.getRandom();

        int mutton = 1 + random.nextInt(2); //1-2

        int string = random.nextInt(2); //0-1

        if (PlumpUtil.isEffectivelyPlump(goat) && ModConfig.get().doPlump == true) {

            self.spawnAtLocation(serverWorld, new ItemStack(
                    Items.MUTTON, (int) (mutton * ModConfig.get().plumpDropsMultiplier) +1
            ));

            if (string > 0) {
                self.spawnAtLocation(serverWorld, new ItemStack(
                        Items.STRING, (int) (string * ModConfig.get().plumpDropsMultiplier) +1
                ));
            }

        } else if (!PlumpUtil.isEffectivelyPlump(goat) || ModConfig.get().doPlump == false) {

            self.spawnAtLocation(serverWorld, new ItemStack(Items.MUTTON, mutton));

            if (string > 0) {
                self.spawnAtLocation(serverWorld, new ItemStack(Items.STRING, string));
            }
        }

    }
}
