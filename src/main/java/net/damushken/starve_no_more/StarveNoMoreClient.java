package net.damushken.starve_no_more;

import net.damushken.starve_no_more.particle.GoldenHeartParticle;
import net.damushken.starve_no_more.particle.ModParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class StarveNoMoreClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ParticleFactoryRegistry.getInstance().register(ModParticles.GOLDEN_HEART_PARTICLE, GoldenHeartParticle.Factory::new);

    }
}
