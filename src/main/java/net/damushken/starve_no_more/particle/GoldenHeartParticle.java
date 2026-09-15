package net.damushken.starve_no_more.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.MathHelper;

public class GoldenHeartParticle extends SpriteBillboardParticle {
    public GoldenHeartParticle(ClientWorld world, double xCoord, double yCoord, double zCoord,
                               SpriteProvider spriteSet, double xd, double yd, double zd) {
        super(world, xCoord, yCoord, zCoord, xd, yd, zd);

        this.ascending = true;

        this.velocityMultiplier = 0.86f;
        this.velocityX *= 0.01f;
        this.velocityY *= 0.01f;
        this.velocityZ *= 0.01f;

        this.velocityY += 0.1f;

        this.scale *= 1.5f;
        this.maxAge = 25;
        this.setSpriteForAge(spriteSet);

        this.collidesWithWorld = false;

        this.red = 1f;
        this.green = 1f;
        this.blue = 1f;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getSize(float tickDelta) {
        return this.scale * MathHelper.clamp((this.age + tickDelta) / this.maxAge * 32.0F, 0.0F, 1.0F);
    }

    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider sprites;

        public Factory(SpriteProvider spriteProvider) {
            this.sprites = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientWorld clientWorld,
                                       double x, double y, double z, double xd, double yd, double zd) {
            return new GoldenHeartParticle(clientWorld, x, y, z, this.sprites, xd, yd, zd);
        }
    }
}
