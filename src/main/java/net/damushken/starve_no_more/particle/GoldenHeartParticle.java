package net.damushken.starve_no_more.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SingleQuadParticle.Layer;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

public class GoldenHeartParticle extends SingleQuadParticle {

    public GoldenHeartParticle(ClientLevel world, double x, double y, double z, TextureAtlasSprite sprite) {
        super(world, x, y, z, sprite);

        this.speedUpWhenYMotionIsBlocked = true;

        this.friction = 0.86f;
        this.xd *= 0.01f;
        this.yd *= 0.01f;
        this.zd *= 0.01f;

        this.yd += 0.1f;

        this.quadSize *= 1.5f;
        this.lifetime = 25;

        this.hasPhysics = false;

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }

    public float getQuadSize(float tickProgress) {
        return this.quadSize * Mth.clamp(((float)this.age + tickProgress) / (float)this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Factory(SpriteSet spriteProvider) {
            this.sprites = spriteProvider;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, RandomSource random) {
            GoldenHeartParticle particle = new GoldenHeartParticle(world, x, y, z, this.sprites.get(random));
            return particle;
        }
    }
}
