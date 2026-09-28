package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.StarveNoMore;
import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.particle.ModParticles;
import net.damushken.starve_no_more.util.PlumpAccess;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Animal.class)
public abstract class AnimalMixin {

    private static final long DAWN_START = 0;

    @Inject(
            method = "finalizeSpawnChildFromBreeding(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/animal/Animal;Lnet/minecraft/world/entity/AgeableMob;)V",
            at = @At("TAIL")
    )
    private void starvenomore$onBreedWithBaby(ServerLevel world, Animal other, AgeableMob baby, CallbackInfo ci) {
        Animal self = (Animal)(Object) this;

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

        long timeOfDay = world.getOverworldClockTime() % 24000L;
        if (timeOfDay < DAWN_START || timeOfDay > DAWN_END) return;

        RandomSource random = self.getRandom();

        int extraSpawned = 0;
        for (int i = 1; i < cfg.dawnBreedableMaxOffsprings; i++) {
            if (random.nextFloat() >= cfg.dawnBreedableOffspringsChance) continue;

            AgeableMob extraChild = self.getBreedOffspring(world, other);
            if (extraChild == null) continue;

            extraChild.setAge(-24000);
            extraChild.snapTo(
                    self.getX(), self.getY(), self.getZ(),
                    0.0F, 0.0F
            );
            if (extraChild instanceof PlumpAccess extraPlump) {
                extraPlump.starvenomore$setPlayerLineage(true);
            }
            world.addFreshEntity(extraChild);

            if (extraChild.level() instanceof ServerLevel serverWorld) {

                serverWorld.sendParticles(ModParticles.GOLDEN_HEART_PARTICLE,
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
