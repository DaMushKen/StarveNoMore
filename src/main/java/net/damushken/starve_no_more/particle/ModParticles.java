package net.damushken.starve_no_more.particle;

import net.damushken.starve_no_more.StarveNoMore;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

public class ModParticles {
    public static final SimpleParticleType GOLDEN_HEART_PARTICLE =
            registerParticle("golden_heart_particle", FabricParticleTypes.simple());


    private static SimpleParticleType registerParticle(String name, SimpleParticleType particleType) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(StarveNoMore.MOD_ID, name), particleType);
    }

    public static void registerParticles() {
        StarveNoMore.LOGGER.info("Registering Particles for " + StarveNoMore.MOD_ID);
    }
}
