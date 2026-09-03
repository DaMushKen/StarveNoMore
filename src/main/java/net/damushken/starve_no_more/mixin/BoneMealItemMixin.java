package net.damushken.starve_no_more.mixin;

import net.damushken.starve_no_more.StarveNoMore;
import net.damushken.starve_no_more.command.ModConfig;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
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

    private static final List<EntityType<? extends PassiveEntity>> BABY_POOL = List.of(
            EntityType.COW, EntityType.PIG, EntityType.SHEEP, EntityType.CHICKEN
    );

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

        EntityType<? extends PassiveEntity> type = BABY_POOL.get(RANDOM.nextInt(BABY_POOL.size()));
        PassiveEntity entity = type.create(world);
        if (entity == null) return;

        entity.refreshPositionAndAngles(
                pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5,
                world.getRandom().nextFloat() * 360.0F, 0.0F
        );
        entity.setBreedingAge(-24000);
        world.spawnEntity(entity);
    }
}
