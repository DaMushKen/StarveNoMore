package net.damushken.starve_no_more.particle;

import net.damushken.starve_no_more.StarveNoMore;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {
    public static final SimpleParticleType GOLDEN_HEART_PARTICLE =
            registerParticle("golden_heart_particle", FabricParticleTypes.simple());


    private static SimpleParticleType registerParticle(String name, SimpleParticleType particleType) {
        return Registry.register(Registries.PARTICLE_TYPE, new Identifier(StarveNoMore.MOD_ID, name), particleType);
    }

    public static void registerParticles() {
        StarveNoMore.LOGGER.info("Registering Particles for " + StarveNoMore.MOD_ID);
    }
}
