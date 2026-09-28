package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.StarveNoMore;
import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.datagen.ModEntityTypeTagProvider;
import net.damushken.starve_no_more.util.ModTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {



    private static final Random RANDOM = new Random();



    @Inject(method = "useOn", at = @At("RETURN"))
    private void starvenomore$onBoneMealUse(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (!cir.getReturnValue().consumesAction()) return;

        Level world = context.getLevel();
        if (world instanceof ClientLevel) return;

        BlockPos pos = context.getClickedPos();
        if (world.getBlockState(pos).getBlock() != Blocks.GRASS_BLOCK) return;

        ModConfig cfg = ModConfig.get();
        if (!cfg.spawnBabyOnBonemeal) return;
        if (RANDOM.nextFloat() >= cfg.spawnBabyOnBonemealChance) return;

        TagKey<EntityType<?>> tag = TagKey.create(Registries.ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(StarveNoMore.MOD_ID, "can_spawn_from_bonemeal"));

        List<EntityType<?>> pool = new java.util.ArrayList<>();
        for (Holder<EntityType<?>> entry : BuiltInRegistries.ENTITY_TYPE.getTagOrEmpty(tag)) {
            pool.add(entry.value());
        }

        if (pool.isEmpty()) return;

        EntityType<?> type = pool.get(RANDOM.nextInt(pool.size()));
        var entity = type.create(world, EntitySpawnReason.TRIGGERED);
        if (!(entity instanceof AgeableMob passiveEntity)) return;

        passiveEntity.snapTo(
                pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                world.getRandom().nextFloat() * 360.0F, 0.0F
        );
        passiveEntity.setAge(-24000);
        world.addFreshEntity(passiveEntity);

        if (passiveEntity.level() instanceof  ServerLevel serverWorld) {

            serverWorld.sendParticles(ParticleTypes.ENCHANT,
                    passiveEntity.getX(), passiveEntity.getY() + 0.5, passiveEntity.getZ(),
                    16, 0.3, 0.5, 0.3, 0.01);

            serverWorld.sendParticles(ParticleTypes.SOUL,
                    passiveEntity.getX(), passiveEntity.getY() + 1.0, passiveEntity.getZ(),
                    1, 0.1, 0.1, 0.1, 0.01);

            serverWorld.playSound(null, passiveEntity.getX(), passiveEntity.getY(), passiveEntity.getZ(),
                    SoundEvents.SUSPICIOUS_GRAVEL_FALL, passiveEntity.getSoundSource(),
                    3.0f, 1.0f);

        }
    }
}
