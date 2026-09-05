package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.StarveNoMore;
import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.util.PlumpAccess;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin {

    // first 5 minutes of the day
    private static final long DAWN_START = 0;
    private static final long DAWN_END = 6000;

    @Inject(
            method = "breed(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/passive/AnimalEntity;Lnet/minecraft/entity/passive/PassiveEntity;)V",
            at = @At("TAIL")
    )
    private void starvenomore$onBreedWithBaby(ServerWorld world, AnimalEntity other, PassiveEntity baby, CallbackInfo ci) {
        AnimalEntity self = (AnimalEntity)(Object) this;

        if (self instanceof PlumpAccess selfPlump) {
            selfPlump.starvenomore$resetBreedTimer();
            selfPlump.starvenomore$setPlayerLineage(true);
        }
        if (other instanceof PlumpAccess otherPlump) {
            otherPlump.starvenomore$resetBreedTimer();
            otherPlump.starvenomore$setPlayerLineage(true);
        }
        if (baby instanceof PlumpAccess babyPlump) {
            babyPlump.starvenomore$setPlayerLineage(true);
        }

        ModConfig cfg = ModConfig.get();
        if (!cfg.dawnBreedableOffsprings) return;

        long timeOfDay = world.getTimeOfDay() % 24000L;
        if (timeOfDay < DAWN_START || timeOfDay > DAWN_END) return;

        Random random = self.getRandom();

        int extraSpawned = 0;
        for (int i = 1; i < cfg.dawnBreedableMaxOffsprings; i++) {
            if (random.nextFloat() >= cfg.dawnBreedableOffspringsChance) continue;

            PassiveEntity extraChild = self.createChild(world, other);
            if (extraChild == null) continue;

            extraChild.setBreedingAge(-24000);
            extraChild.refreshPositionAndAngles(
                    self.getX(), self.getY(), self.getZ(),
                    0.0F, 0.0F
            );
            if (extraChild instanceof PlumpAccess extraPlump) {
                extraPlump.starvenomore$setPlayerLineage(true);
            }
            world.spawnEntity(extraChild);
            extraSpawned++;
        }

        if (extraSpawned > 0) {
            StarveNoMore.LOGGER.info("Dawn breeding bonus: +{} extra offspring for {}",
                    extraSpawned, self.getType());
        }
    }
}
