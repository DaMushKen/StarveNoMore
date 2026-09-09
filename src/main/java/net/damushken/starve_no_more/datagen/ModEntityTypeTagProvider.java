package net.damushken.starve_no_more.datagen;

import net.damushken.starve_no_more.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagProvider extends FabricTagProvider.EntityTypeTagProvider {
    public ModEntityTypeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {

        getOrCreateTagBuilder(ModTags.EntityTypes.CAN_PLUMP)

                .add(EntityType.COW)
                .add(EntityType.GOAT)
                .add(EntityType.MOOSHROOM)
                .add(EntityType.SHEEP)
                .add(EntityType.PIG)
                .add(EntityType.CHICKEN)
                .add(EntityType.RABBIT)
                .add(EntityType.HOGLIN);



        getOrCreateTagBuilder(ModTags.EntityTypes.CAN_SPAWN_FROM_BONEMEAL)
                .add(EntityType.PIG)
                .add(EntityType.SHEEP)
                .add(EntityType.CHICKEN)
                .add(EntityType.COW)
                .add(EntityType.RABBIT);

    }
}
