package net.damushken.starve_no_more.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;

public class GoldenHeartParticle extends BillboardParticle {

    public GoldenHeartParticle(ClientWorld world, double x, double y, double z, Sprite sprite) {
        super(world, x, y, z, sprite);

        this.ascending = true;

        this.velocityMultiplier = 0.86f;
        this.velocityX *= 0.01f;
        this.velocityY *= 0.01f;
        this.velocityZ *= 0.01f;

        this.velocityY += 0.1f;

        this.scale *= 1.5f;
        this.maxAge = 25;

        this.collidesWithWorld = false;

        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
    }

    @Override
    protected RenderType getRenderType() {
        return RenderType.PARTICLE_ATLAS_OPAQUE;
    }

    public float getSize(float tickProgress) {
        return this.scale * MathHelper.clamp(((float)this.age + tickProgress) / (float)this.maxAge * 32.0F, 0.0F, 1.0F);
    }

    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteProvider) {
            this.sprites = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
            GoldenHeartParticle particle = new GoldenHeartParticle(world, x, y, z, this.sprites.getSprite(random));
            return particle;
        }
    }
}
