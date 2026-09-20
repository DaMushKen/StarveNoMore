package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.StarveNoMore;
import net.damushken.starve_no_more.command.ModConfig;
import net.damushken.starve_no_more.datagen.ModEntityTypeTagProvider;
import net.damushken.starve_no_more.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Random;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {



    private static final Random RANDOM = new Random();



    @Inject(method = "useOnBlock", at = @At("RETURN"))
    private void starvenomore$onBoneMealUse(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (!cir.getReturnValue().isAccepted()) return;

        World world = context.getWorld();
        if (world.isClient) return;

        BlockPos pos = context.getBlockPos();
        if (world.getBlockState(pos).getBlock() != Blocks.GRASS_BLOCK) return;

        ModConfig cfg = ModConfig.get();
        if (!cfg.spawnBabyOnBonemeal) return;
        if (RANDOM.nextFloat() >= cfg.spawnBabyOnBonemealChance) return;

        TagKey<EntityType<?>> tag = TagKey.of(RegistryKeys.ENTITY_TYPE,
                Identifier.of(StarveNoMore.MOD_ID, "can_spawn_from_bonemeal"));

        List<EntityType<?>> pool = new java.util.ArrayList<>();
        for (RegistryEntry<EntityType<?>> entry : Registries.ENTITY_TYPE.iterateEntries(tag)) {
            pool.add(entry.value());
        }

        if (pool.isEmpty()) return;

        EntityType<?> type = pool.get(RANDOM.nextInt(pool.size()));
        var entity = type.create(world, SpawnReason.TRIGGERED);
        if (!(entity instanceof PassiveEntity passiveEntity)) return;

        passiveEntity.refreshPositionAndAngles(
                pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                world.getRandom().nextFloat() * 360.0F, 0.0F
        );
        passiveEntity.setBreedingAge(-24000);
        world.spawnEntity(passiveEntity);

        if (passiveEntity.getWorld() instanceof  ServerWorld serverWorld) {

            serverWorld.spawnParticles(ParticleTypes.ENCHANT,
                    passiveEntity.getX(), passiveEntity.getY() + 0.5, passiveEntity.getZ(),
                    16, 0.3, 0.5, 0.3, 0.01);

            serverWorld.spawnParticles(ParticleTypes.SOUL,
                    passiveEntity.getX(), passiveEntity.getY() + 1.0, passiveEntity.getZ(),
                    1, 0.1, 0.1, 0.1, 0.01);

            serverWorld.playSound(null, passiveEntity.getX(), passiveEntity.getY(), passiveEntity.getZ(),
                    SoundEvents.BLOCK_SUSPICIOUS_GRAVEL_FALL, passiveEntity.getSoundCategory(),
                    3.0f, 1.0f);

        }
    }
}
