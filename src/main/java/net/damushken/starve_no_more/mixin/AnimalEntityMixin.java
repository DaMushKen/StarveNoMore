package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.StarveNoMore;
import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.particle.ModParticles;
import net.damushken.starve_no_more.util.PlumpAccess;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimalEntity.class)
public abstract class AnimalEntityMixin {

    private static final long DAWN_START = 0;

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

        int DAWN_END = cfg.dawnBreedableMaxTicks;

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

            if (extraChild.getWorld() instanceof ServerWorld serverWorld) {

                serverWorld.spawnParticles(ModParticles.GOLDEN_HEART_PARTICLE,
                        extraChild.getX(), extraChild.getY() + 1.5, extraChild.getZ(),
                        3, 0.25, 0.5, 0.25, 0.05);

            }

            extraSpawned++;
        }

        if (extraSpawned > 0) {
            StarveNoMore.LOGGER.info("Dawn breeding bonus: +{} extra offspring for {}",
                    extraSpawned, self.getType());
        }
    }
}
